package com.sequenceiq.cloudbreak.cloud.polling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPollingService implements PollingService<PollingInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPollingService.class);

    @Override
    public PollingInfo doPoll(PollingInfo persistedPollingInfo) {
        //todo check the max number of polling cycles / timeout ?
        PollingInfo freshPollingInfo = null;
        LOGGER.debug("Polling invoked with persisted polling info: {}", persistedPollingInfo);

        if (isTransient(persistedPollingInfo)) {
            LOGGER.debug("The persisted polling info is in transient state: {}", persistedPollingInfo);

            freshPollingInfo = fetchCloudPollingInfo(persistedPollingInfo);
            LOGGER.debug("Retrieved fresh resource status info: {}", freshPollingInfo);

            if (isSuccess(freshPollingInfo)) {
                LOGGER.debug("Polling success; fresh polling info: {}", freshPollingInfo);
                return handleSuccess(freshPollingInfo);
            } else {
                LOGGER.debug("Polling not yet finished; fresh polling info: {}", freshPollingInfo);
                return handleFailure(freshPollingInfo);
            }
        } else {
            LOGGER.debug("The persisted polling info is not active: {}", persistedPollingInfo);
            return handlePermanentPollingInfo(persistedPollingInfo);
        }
    }

    protected abstract boolean isSuccess(PollingInfo freshPollingInfo);

    protected abstract boolean isTransient(PollingInfo persistedPollingInfo);

    protected abstract PollingInfo fetchCloudPollingInfo(PollingInfo pollingInfo);

}
