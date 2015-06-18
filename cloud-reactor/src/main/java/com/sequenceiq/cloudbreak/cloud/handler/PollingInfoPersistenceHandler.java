package com.sequenceiq.cloudbreak.cloud.handler;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;
import com.sequenceiq.cloudbreak.cloud.notification.PollingNotifier;
import com.sequenceiq.cloudbreak.cloud.notification.model.PollingNotification;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;
import com.sequenceiq.cloudbreak.cloud.polling.PollingService;
import com.sequenceiq.cloudbreak.cloud.service.Persister;

import reactor.bus.Event;

@Component
public class PollingInfoPersistenceHandler implements CloudPlatformEventHandler<PollingNotification> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingInfoPersistenceHandler.class);

    @Inject
    private Persister<PollingInfo> pollingNotificationPersister;

    @Inject
    private PollingService pollingService;

    @Inject
    private PollingNotifier pollingNotifier;

    @Override
    public void accept(Event<PollingNotification> pollingNotificationEvent) {
        LOGGER.info("Polling notification received: {}", pollingNotificationEvent);
        PollingInfo pollingInfo = (PollingInfo) pollingNotificationEvent.getData().pollingInfo();
        if (pollingInfo.pollingReference() == null) {
            LOGGER.debug("New polling data received, persisting it: {}", pollingInfo);
            pollingInfo.setPollingStatus(ResourceStatus.IN_PROGRESS);
            pollingInfo = pollingNotificationPersister.persist(pollingInfo);
        } else {
            LOGGER.debug("Active polling data received, retrieving it: {}", pollingInfo);
            pollingInfo = pollingNotificationPersister.retrieve(pollingInfo);
        }
        LOGGER.debug("Persisted polling information available: {}", pollingInfo);
        pollingNotifier.pollingInfoPersisted(pollingInfo);
    }

    @Override
    public Class<PollingNotification> type() {
        return PollingNotification.class;
    }
}
