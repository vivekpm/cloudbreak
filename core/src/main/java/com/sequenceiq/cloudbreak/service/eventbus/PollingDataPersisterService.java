package com.sequenceiq.cloudbreak.service.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfoFactory;
import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;
import com.sequenceiq.cloudbreak.domain.PollingData;

@Component
public class PollingDataPersisterService extends AbstractCloudPersisterService<PollingInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingDataPersisterService.class);

    @Override
    public PollingInfo persist(PollingInfo pollingInfo) {
        LOGGER.debug("Persisting polling notification data : {}", pollingInfo);
        PollingData pollingData = getConversionService().convert(pollingInfo, PollingData.class);
        pollingData = (PollingData) getRepositoryForEntity(pollingData).save(pollingData);
        ResourcePollingInfo persistedPollingInfo = getConversionService().convert(pollingData, ResourcePollingInfo.class);
        return PollingInfoFactory.transferContextualInfo((ResourcePollingInfo) pollingInfo, persistedPollingInfo);
    }

    @Override
    public PollingInfo retrieve(PollingInfo pollingInfo) {
        LOGGER.debug("Retrieving polling notification data : {}", pollingInfo);
        PollingData pollingData = getConversionService().convert(pollingInfo, PollingData.class);
        pollingData = (PollingData) getRepositoryForEntity(pollingData).findOne(pollingData.getId());
        ResourcePollingInfo persistedPollingInfo = getConversionService().convert(pollingData, ResourcePollingInfo.class);
        return PollingInfoFactory.transferContextualInfo((ResourcePollingInfo) pollingInfo, persistedPollingInfo);
    }

}
