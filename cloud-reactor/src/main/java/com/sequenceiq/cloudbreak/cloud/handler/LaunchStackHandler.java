package com.sequenceiq.cloudbreak.cloud.handler;


import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.CloudConnector;
import com.sequenceiq.cloudbreak.cloud.event.resource.LaunchStackRequest;
import com.sequenceiq.cloudbreak.cloud.event.resource.LaunchStackResult;
import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.init.CloudPlatformConnectors;
import com.sequenceiq.cloudbreak.cloud.model.CloudResource;
import com.sequenceiq.cloudbreak.cloud.model.CloudResourceStatus;
import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;
import com.sequenceiq.cloudbreak.cloud.notification.ResourcePersistenceNotifier;
import com.sequenceiq.cloudbreak.cloud.scheduler.SyncPollingScheduler;
import com.sequenceiq.cloudbreak.cloud.task.PollTask;
import com.sequenceiq.cloudbreak.cloud.task.PollTaskFactory;
import com.sequenceiq.cloudbreak.cloud.transform.LaunchStackResults;
import com.sequenceiq.cloudbreak.cloud.transform.ResourceLists;

import reactor.bus.Event;

@Component
public class LaunchStackHandler implements CloudPlatformEventHandler<LaunchStackRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchStackHandler.class);

    private static final int INTERVAL = 5;
    private static final int MAX_ATTEMPT = 100;

    @Inject
    private CloudPlatformConnectors cloudPlatformConnectors;

    @Inject
    private SyncPollingScheduler<LaunchStackResult> syncPollingScheduler;

    @Inject
    private PollTaskFactory statusCheckFactory;

    @Inject
    private ResourcePersistenceNotifier resourcePersistenceNotifier;

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

            List<CloudResourceStatus> resourceStatus = connector.resources().launch(ac, launchStackRequest.getCloudStack(),
                    resourcePersistenceNotifier);

            List<CloudResource> resources = ResourceLists.transform(resourceStatus);

            PollTask<LaunchStackResult> task = statusCheckFactory.newPollResourcesStateTask(ac, resources);
            LaunchStackResult launchStackResult = LaunchStackResults.build(launchStackRequest.getCloudContext(), resourceStatus);
            if (!task.completed(launchStackResult)) {
                launchStackResult = syncPollingScheduler.schedule(task, INTERVAL, MAX_ATTEMPT);
            }

            launchStackRequest.getResult().onNext(launchStackResult);

        } catch (Exception e) {
            LOGGER.error("Failed to handle LaunchStackRequest. Error: ", e);
            launchStackRequest.getResult().onNext(new LaunchStackResult(launchStackRequest.getCloudContext(), ResourceStatus.FAILED, e.getMessage(), null));
        }
        LOGGER.info("LaunchStackHandler finished");
    }


}