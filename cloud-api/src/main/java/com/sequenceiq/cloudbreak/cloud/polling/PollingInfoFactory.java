package com.sequenceiq.cloudbreak.cloud.polling;

import java.util.List;

import com.sequenceiq.cloudbreak.cloud.CloudConnector;
import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudResource;

public class PollingInfoFactory {
    private PollingInfoFactory() {
    }

    public static ResourcePollingInfo createResourcePollingInfo(CloudConnector cloudConnector, AuthenticatedContext authenticatedContext,
            List<CloudResource> cloudResource) {
        ResourcePollingInfo resourcePollingInfo = new ResourcePollingInfo();
        resourcePollingInfo.setConnector(cloudConnector);
        resourcePollingInfo.setAuthenticatedContext(authenticatedContext);
        resourcePollingInfo.setCloudResource(cloudResource);
        return resourcePollingInfo;
    }

    public static PollingInfo transferContextualInfo(ResourcePollingInfo acAwarePollingInfo, ResourcePollingInfo persistedPollingInfo) {
        persistedPollingInfo.setAuthenticatedContext(acAwarePollingInfo.authenticatedContext());
        persistedPollingInfo.setCloudResource(acAwarePollingInfo.getCloudResource());
        persistedPollingInfo.setConnector(acAwarePollingInfo.getConnector());
        return persistedPollingInfo;
    }
}
