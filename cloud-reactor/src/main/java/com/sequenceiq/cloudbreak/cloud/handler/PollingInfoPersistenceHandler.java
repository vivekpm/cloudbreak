package com.sequenceiq.cloudbreak.cloud.handler;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;
import com.sequenceiq.cloudbreak.cloud.notification.CloudbreakNotification;
import com.sequenceiq.cloudbreak.cloud.notification.StateDispatcherNotifier;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;
import com.sequenceiq.cloudbreak.cloud.service.Persister;

@Component
public class PollingInfoPersistenceHandler extends DispatchingEventHandler<CloudbreakNotification> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingInfoPersistenceHandler.class);

    @Inject
    private Persister<PollingInfo> pollingNotificationPersister;

    @Inject
    private StateDispatcherNotifier stateDispatcherNotifier;


    @Override
    protected ReactorStateContext process(CloudbreakNotification payload) {
        LOGGER.info("Polling notification received: {}", payload);
        PollingInfo pollingInfo = (PollingInfo) payload.payload();
        if (pollingInfo.pollingReference() == null) {
            LOGGER.debug("New polling data received, persisting it: {}", pollingInfo);
            pollingInfo.setPollingStatus(ResourceStatus.IN_PROGRESS);
            pollingInfo = pollingNotificationPersister.persist(pollingInfo);
        } else {
            LOGGER.debug("Active polling data received, retrieving it: {}", pollingInfo);
            pollingInfo = pollingNotificationPersister.retrieve(pollingInfo);
        }
        LOGGER.debug("Persisted polling information available: {}", pollingInfo);
        return ReactorStateContextFactory.createReactorStateContext(getClass(), pollingInfo);
    }

    @Override
    public Class<CloudbreakNotification> type() {
        return CloudbreakNotification.class;
    }
}
