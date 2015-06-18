package com.sequenceiq.cloudbreak.cloud.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.cloudbreak.cloud.notification.model.PollingNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.PollingResultNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.ResourcePollingNotification;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;
import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;

public class PollingNotificationFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingNotificationFactory.class);

    private PollingNotificationFactory() {
    }

    public static PollingNotification createPollingNotification(PollingInfo pollingInfo) {
        LOGGER.debug("Creating polling notification with: {}", pollingInfo);
        if (pollingInfo instanceof ResourcePollingInfo) {
            return createResourcePollingNotification(pollingInfo);
        } else {
            throw new IllegalStateException("Unsupported Polling info type: " + pollingInfo);
        }
    }

    public static PollingNotification createPollingResultNotification(PollingInfo pollingInfo) {
        LOGGER.debug("Creating polling result notification with: {}", pollingInfo);
        return new PollingResultNotification(pollingInfo);
    }

    private static ResourcePollingNotification createResourcePollingNotification(PollingInfo pollingInfo) {
        LOGGER.debug("Creating resource polling notification with: {}", pollingInfo);
        return new ResourcePollingNotification((ResourcePollingInfo) pollingInfo);
    }


}
