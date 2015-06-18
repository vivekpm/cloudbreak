package com.sequenceiq.cloudbreak.cloud.notification.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.cloudbreak.cloud.notification.CloudbreakNotification;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;

public class PollingResultNotification implements CloudbreakNotification<PollingInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingResultNotification.class);

    private PollingInfo pollingInfo;

    public PollingResultNotification(PollingInfo pollingInfo) {
        this.pollingInfo = pollingInfo;
    }

    @Override
    public PollingInfo payload() {
        return pollingInfo;
    }

    //BEGIN GENERATED CODE

    @Override
    public String toString() {
        return "PollingResultNotification{" +
                "pollingInfo=" + pollingInfo +
                '}';
    }

    //END GENERATED CODE

}
