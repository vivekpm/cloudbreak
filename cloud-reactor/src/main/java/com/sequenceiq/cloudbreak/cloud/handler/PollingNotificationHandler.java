package com.sequenceiq.cloudbreak.cloud.handler;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.notification.model.PollingNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.PollingResultNotification;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;
import com.sequenceiq.cloudbreak.cloud.polling.PollingService;
import com.sequenceiq.cloudbreak.cloud.service.Persister;

@Component
public class PollingNotificationHandler extends DispatchingEventHandler<PollingNotification> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingNotificationHandler.class);

    @Inject
    private PollingService<PollingInfo> pollingService;

    @Inject
    private Persister<PollingInfo> pollingNotificationPersister;

    @Override
    public Class<PollingNotification> type() {
        return PollingNotification.class;
    }

    @Override
    protected ReactorStateContext process(PollingNotification payload) {
        LOGGER.debug("Performing polling cycle based on: {}", payload);
        PollingInfo freshPollingInfo = pollingService.doPoll(payload.payload());
        freshPollingInfo.increasePollingCycle();
        freshPollingInfo = pollingNotificationPersister.persist(freshPollingInfo);
        PollingResultNotification pollingResultNotification = NotificationFactory.createPollingResultNotification(freshPollingInfo);
        return ReactorStateContextFactory.createReactorStateContext(getClass(), pollingResultNotification);
    }
}
