package com.sequenceiq.cloudbreak.cloud.polling;

import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;

/**
 * Marker for specific polling notification objects.
 */
public interface PollingInfo {

    PollingReference pollingReference();

    //todo naming!
    ResourceStatus pollingStatus();

    void setPollingStatus(ResourceStatus pollingStatus);

    void increasePollingCycle();

    int pollingCycle();

    AuthenticatedContext authenticatedContext();
}
