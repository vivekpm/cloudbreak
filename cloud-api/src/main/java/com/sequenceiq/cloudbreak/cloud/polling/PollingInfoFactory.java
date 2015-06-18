package com.sequenceiq.cloudbreak.cloud.polling;

import java.util.List;

import com.sequenceiq.cloudbreak.cloud.CloudConnector;
import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudResourceStatus;

import reactor.rx.Promise;

public class PollingInfoFactory {
    private PollingInfoFactory() {
    }

    public static PollingInfo createResourcePollingInfo(CloudConnector cloudConnector, AuthenticatedContext authenticatedContext,
            List<CloudResourceStatus> cloudResourceStatus) {
        ResourcePollingInfo resourcePollingInfo = new ResourcePollingInfo();
        resourcePollingInfo.setConnector(cloudConnector);
        resourcePollingInfo.setAuthenticatedContext(authenticatedContext);
        resourcePollingInfo.setCloudResourceStatus(cloudResourceStatus);
        // the reference is the stackid! it should be the cloudreference
        resourcePollingInfo.setPollingReference(new NumericPollingReference(authenticatedContext.getCloudContext().getStackId()));
        return resourcePollingInfo;
    }

    public static PollingInfo transferContextualInfo(ResourcePollingInfo acAwarePollingInfo, ResourcePollingInfo persistedPollingInfo) {
        persistedPollingInfo.setAuthenticatedContext(acAwarePollingInfo.authenticatedContext());
        persistedPollingInfo.setCloudResourceStatus(acAwarePollingInfo.getCloudResourceStatus());
        persistedPollingInfo.setConnector(acAwarePollingInfo.getConnector());
        persistedPollingInfo.setPromise(acAwarePollingInfo.getPromise());
        return persistedPollingInfo;
    }

    public static ResourcePollingInfo createPromiseAwarePollingInfo(CloudConnector cloudConnector, AuthenticatedContext authenticatedContext,
            List<CloudResourceStatus> cloudResourceStatus, Promise promise) {
        ResourcePollingInfo promiseAwarePollingInfo = (ResourcePollingInfo) createResourcePollingInfo(cloudConnector, authenticatedContext,
                cloudResourceStatus);
        promiseAwarePollingInfo.setPromise(promise);
        return promiseAwarePollingInfo;
    }
}
