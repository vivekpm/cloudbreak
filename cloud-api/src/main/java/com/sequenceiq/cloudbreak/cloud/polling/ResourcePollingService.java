package com.sequenceiq.cloudbreak.cloud.polling;

public class ResourcePollingService extends AbstractPollingService {
    @Override
    protected boolean isSuccess(PollingInfo freshPollingInfo) {
        return false;
    }

    @Override
    protected boolean isTransient(PollingInfo persistedPollingInfo) {
        return false;
    }

    @Override
    protected PollingInfo fetchCloudPollingInfo(PollingInfo pollingInfo) {
        return null;
    }

    @Override
    public PollingInfo handleSuccess(PollingInfo freshPollingInfo) {
        return null;
    }

    @Override
    public PollingInfo handleFailure(PollingInfo freshPollingInfo) {
        return null;
    }

    @Override
    public PollingInfo handlePermanentPollingInfo(PollingInfo pollingInfo) {
        return null;
    }
}
