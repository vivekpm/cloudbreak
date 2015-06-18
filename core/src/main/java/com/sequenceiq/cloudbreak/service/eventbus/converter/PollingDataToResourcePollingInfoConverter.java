package com.sequenceiq.cloudbreak.service.eventbus.converter;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;
import com.sequenceiq.cloudbreak.cloud.polling.NumericPollingReference;
import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;
import com.sequenceiq.cloudbreak.converter.AbstractConversionServiceAwareConverter;
import com.sequenceiq.cloudbreak.domain.PollingData;

@Component
public class PollingDataToResourcePollingInfoConverter extends AbstractConversionServiceAwareConverter<PollingData, ResourcePollingInfo> {

    @Override
    public ResourcePollingInfo convert(PollingData source) {
        ResourcePollingInfo resourcePollingInfo = new ResourcePollingInfo();
        resourcePollingInfo.setPollingStatus(ResourceStatus.valueOf(source.getStatus()));
        resourcePollingInfo.setPollingReference(new NumericPollingReference(source.getId()));
        resourcePollingInfo.setPollingCycle(source.getNumberOfPolls());
        return resourcePollingInfo;
    }
}
