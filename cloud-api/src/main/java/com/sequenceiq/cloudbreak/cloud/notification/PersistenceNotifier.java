package com.sequenceiq.cloudbreak.cloud.notification;

import com.sequenceiq.cloudbreak.cloud.notification.model.ResourceAllocationNotification;

public interface PersistenceNotifier {
    void notifyResourceAllocation(ResourceAllocationNotification resourceAllocationNotification);
}
