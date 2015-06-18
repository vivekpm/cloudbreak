package com.sequenceiq.cloudbreak.cloud.transform;

import java.util.List;

import com.sequenceiq.cloudbreak.cloud.event.context.CloudContext;
import com.sequenceiq.cloudbreak.cloud.event.resource.LaunchStackResult;
import com.sequenceiq.cloudbreak.cloud.model.CloudResourceStatus;
import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;

public class LaunchStackResults {

    private LaunchStackResults() {
    }

    public static LaunchStackResult build(CloudContext context, List<CloudResourceStatus> results) {
        CloudResourceStatus status = ResourceStatusLists.aggregate(results);
        return new LaunchStackResult(context, status.getStatus(), status.getStatusReason(), results);
    }

    public static LaunchStackResult build(ResourcePollingInfo pollingInfo) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

}
