package com.sequenceiq.cloudbreak.cloud.handler;

import com.sequenceiq.cloudbreak.cloud.notification.model.PollingNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.PollingResultNotification;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;

public class NotificationFactory {
    private NotificationFactory() {

    }

    public static PollingNotification createPollingNotification(PollingInfo pollingInfo) {
        return new PollingNotification(pollingInfo);
    }

    public static PollingResultNotification createPollingResultNotification(PollingInfo pollingInfo) {
        return new PollingResultNotification(pollingInfo);
    }
}
