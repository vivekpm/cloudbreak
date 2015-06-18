package com.sequenceiq.cloudbreak.cloud.notification.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;

public class ResourcePollingNotification implements PollingNotification<ResourcePollingInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcePollingNotification.class);

    private ResourcePollingInfo resourcePollingInfo;

    public ResourcePollingNotification(ResourcePollingInfo resourcePollingInfo) {
        this.resourcePollingInfo = resourcePollingInfo;
    }

    @Override
    public ResourcePollingInfo pollingInfo() {
        return resourcePollingInfo;
    }

    @Override
    public void operationCompleted(ResourcePollingInfo pollingInfo) {
        LOGGER.debug("TBD / operation completed: {}", pollingInfo);
    }

    //BEGIN GENERATED CODE

    @Override
    public String toString() {
        return "ResourcePollingNotification{" +
                "resourcePollingInfo=" + resourcePollingInfo +
                '}';
    }

    //END GENERATED CODE

}
