package com.sequenceiq.cloudbreak.service.eventbus.converter;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;
import com.sequenceiq.cloudbreak.converter.AbstractConversionServiceAwareConverter;
import com.sequenceiq.cloudbreak.domain.PollingData;

@Component
public class ResourcePollingInfoToPollingDataConverter extends AbstractConversionServiceAwareConverter<ResourcePollingInfo, PollingData> {

    @Override
    public PollingData convert(ResourcePollingInfo source) {
        PollingData pollingData = new PollingData();
        pollingData.setStatus(source.pollingStatus().toString());
        if (null != source.pollingReference()) {
            pollingData.setId(source.pollingReference().referenceData());
        }
        pollingData.setNumberOfPolls(source.pollingCycle());
        pollingData.setStackId(source.pollingReference().referenceData());
        return pollingData;
    }
}
