package com.sequenceiq.cloudbreak.cloud.polling;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.cloud.model.CloudResourceStatus;
import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;
import com.sequenceiq.cloudbreak.cloud.transform.ResourceStatusLists;

@Service
public class EventBasedResourcePollingService extends AbstractPollingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBasedResourcePollingService.class);

    @Override
    protected boolean isSuccess(PollingInfo freshPollingInfo) {
        LOGGER.debug("Determine success ...");
        return freshPollingInfo.pollingStatus().equals(ResourceStatus.CREATED);

    }

    @Override
    protected boolean isTransient(PollingInfo persistedPollingInfo) {
        return persistedPollingInfo.pollingStatus().isTransient();
    }

    @Override
    protected PollingInfo fetchCloudPollingInfo(PollingInfo pollingInfo) {
        LOGGER.debug("Returning the passed in pollinginfo: {}", pollingInfo);
        ResourcePollingInfo resourcePollingInfo = (ResourcePollingInfo) pollingInfo;
        List<CloudResourceStatus> results = resourcePollingInfo.getConnector().resources()
                .check(resourcePollingInfo.authenticatedContext(), resourcePollingInfo.getCloudResource());
        CloudResourceStatus status = ResourceStatusLists.aggregate(results);
        pollingInfo.setPollingStatus(status.getStatus());
        return pollingInfo;
    }

    @Override
    public PollingInfo handleSuccess(PollingInfo pollingInfo) {
        LOGGER.debug("Handling success for pollinginfo: {}", pollingInfo);
        return pollingInfo;
    }

    @Override
    public PollingInfo handleFailure(PollingInfo pollingInfo) {
        LOGGER.debug("Handling success for pollinginfo: {}", pollingInfo);
        return pollingInfo;
    }

    @Override
    public PollingInfo handlePermanentPollingInfo(PollingInfo pollingInfo) {
        LOGGER.debug("Handling pollinginfo in permanent status: {}", pollingInfo);
        return pollingInfo;
    }
}
