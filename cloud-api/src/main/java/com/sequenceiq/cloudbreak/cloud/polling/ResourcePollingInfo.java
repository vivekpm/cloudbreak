package com.sequenceiq.cloudbreak.cloud.polling;

import java.util.List;

import com.sequenceiq.cloudbreak.cloud.CloudConnector;
import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudResource;
import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;

public class ResourcePollingInfo implements PollingInfo {

    private NumericPollingReference pollingReference;
    private ResourceStatus pollingStatus = ResourceStatus.IN_PROGRESS;
    private int pollingCycle;

    private CloudConnector connector;
    private AuthenticatedContext authenticatedContext;
    private List<CloudResource> cloudResource;

    public ResourcePollingInfo() {
    }

    @Override
    public NumericPollingReference pollingReference() {
        return pollingReference;
    }

    @Override
    public ResourceStatus pollingStatus() {
        return pollingStatus;
    }

    @Override
    public void setPollingStatus(ResourceStatus pollingStatus) {
        this.pollingStatus = pollingStatus;
    }

    @Override
    public void increasePollingCycle() {
        this.pollingCycle++;
    }

    @Override
    public int pollingCycle() {
        return pollingCycle;
    }

    @Override
    public AuthenticatedContext authenticatedContext() {
        return authenticatedContext;
    }

    public void setConnector(CloudConnector connector) {
        this.connector = connector;
    }

    public void setAuthenticatedContext(AuthenticatedContext authenticatedContext) {
        this.authenticatedContext = authenticatedContext;
    }

    public void setCloudResource(List<CloudResource> cloudResource) {
        this.cloudResource = cloudResource;
    }

    public void setPollingReference(NumericPollingReference pollingReference) {
        this.pollingReference = pollingReference;
    }

    public void setPollingCycle(int pollingCycle) {
        this.pollingCycle = pollingCycle;
    }

    public List<CloudResource> getCloudResource() {
        return cloudResource;
    }

    public CloudConnector getConnector() {
        return connector;
    }

    //BEGIN GENERATED CODE

    @Override
    public String toString() {
        return "ResourcePollingInfo{" +
                "pollingReference=" + pollingReference +
                ", pollingStatus=" + pollingStatus +
                ", pollingCycle=" + pollingCycle +
                ", connector=" + connector +
                ", authenticatedContext=" + authenticatedContext +
                ", cloudResource=" + cloudResource +
                '}';
    }

    //END GENERATED CODE
}
