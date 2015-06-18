package com.sequenceiq.cloudbreak.cloud.handler;


import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.CloudConnector;
import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.event.resource.LaunchStackRequest;
import com.sequenceiq.cloudbreak.cloud.event.resource.LaunchStackResult;
import com.sequenceiq.cloudbreak.cloud.init.CloudPlatformConnectors;
import com.sequenceiq.cloudbreak.cloud.model.CloudResourceStatus;
import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;
import com.sequenceiq.cloudbreak.cloud.notification.PersistenceNotifier;
import com.sequenceiq.cloudbreak.cloud.notification.StateDispatcherNotifier;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfoFactory;
import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;
import com.sequenceiq.cloudbreak.cloud.scheduler.SyncPollingScheduler;
import com.sequenceiq.cloudbreak.cloud.task.PollTaskFactory;
import com.sequenceiq.cloudbreak.cloud.task.ResourcesStatePollerResult;
import com.sequenceiq.cloudbreak.cloud.transform.ResourceLists;
import com.sequenceiq.cloudbreak.cloud.transform.ResourcesStatePollerResults;
import com.sequenceiq.cloudbreak.cloud.transform.LaunchStackResults;

import reactor.bus.Event;
import reactor.rx.Promise;
import reactor.rx.Promises;

@Component
public class LaunchStackHandler implements CloudPlatformEventHandler<LaunchStackRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchStackHandler.class);

    private static final int INTERVAL = 5;
    private static final int MAX_ATTEMPT = 100;

    @Inject
    private CloudPlatformConnectors cloudPlatformConnectors;

    @Inject
    private SyncPollingScheduler<ResourcesStatePollerResult> syncPollingScheduler;

    @Inject
    private PollTaskFactory statusCheckFactory;

    @Inject
    private PersistenceNotifier resourcePersistenceNotifier;

    @Inject
    private StateDispatcherNotifier stateDispatcherNotifier;

    @Override
    public Class<LaunchStackRequest> type() {
        return LaunchStackRequest.class;
    }

    @Override
    public void accept(Event<LaunchStackRequest> launchStackRequestEvent) {
        LOGGER.info("Received event: {}", launchStackRequestEvent);
        LaunchStackRequest launchStackRequest = launchStackRequestEvent.getData();
        try {
            String platform = launchStackRequest.getCloudContext().getPlatform();
            CloudConnector connector = cloudPlatformConnectors.get(platform);
            AuthenticatedContext ac = connector.authenticate(launchStackRequest.getCloudContext(), launchStackRequest.getCloudCredential());

            LOGGER.debug("Launching cloud stack. Request: {}", launchStackRequest);
            List<CloudResourceStatus> resourceStatus = connector.resources().launch(ac, launchStackRequest.getCloudStack(),
                    resourcePersistenceNotifier);

            Promise pollingPromise = Promises.prepare();

            LOGGER.debug("Triggering resourse status polling for resources: {}", resourceStatus);
            stateDispatcherNotifier.startPolling(PollingInfoFactory.createPromiseAwarePollingInfo(connector, ac, resourceStatus, pollingPromise));

            PollingInfo pollingResult = (PollingInfo) pollingPromise.await(1, TimeUnit.HOURS);

            LOGGER.debug("Resource polling terminated: {}", pollingResult);
            launchStackRequest.getResult().onNext(LaunchStackResults.build(launchStackRequest.getCloudContext(),
                    ((ResourcePollingInfo) pollingResult).getCloudResourceStatus()));

        } catch (Exception e) {
            LOGGER.error("Failed to handle LaunchStackRequest. Error: ", e);
            launchStackRequest.getResult().onNext(new LaunchStackResult(launchStackRequest.getCloudContext(), ResourceStatus.FAILED, e.getMessage(), null));
        }
        LOGGER.info("LaunchStackHandler finished");
    }


}
