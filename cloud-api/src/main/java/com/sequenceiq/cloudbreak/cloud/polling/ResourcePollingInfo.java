package com.sequenceiq.cloudbreak.cloud.polling;

import java.util.List;

import com.sequenceiq.cloudbreak.cloud.CloudConnector;
import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudResourceStatus;
import com.sequenceiq.cloudbreak.cloud.model.ResourceStatus;

import reactor.rx.Promise;

public class ResourcePollingInfo implements PollingInfo {

    private NumericPollingReference pollingReference;
    private ResourceStatus pollingStatus = ResourceStatus.IN_PROGRESS;
    private int pollingCycle;

    private CloudConnector connector;
    private AuthenticatedContext authenticatedContext;
    private List<CloudResourceStatus> cloudResourceStatus;

    private Promise<PollingInfo> promise;

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

    public void setCloudResourceStatus(List<CloudResourceStatus> cloudResourceStatus) {
        this.cloudResourceStatus = cloudResourceStatus;
    }

    public void setPollingReference(NumericPollingReference pollingReference) {
        this.pollingReference = pollingReference;
    }

    public void setPollingCycle(int pollingCycle) {
        this.pollingCycle = pollingCycle;
    }

    public List<CloudResourceStatus> getCloudResourceStatus() {
        return cloudResourceStatus;
    }

    public CloudConnector getConnector() {
        return connector;
    }

    public Promise<PollingInfo> getPromise() {
        return promise;
    }

    public void setPromise(Promise<PollingInfo> promise) {
        this.promise = promise;
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
                ", cloudResourceStatus=" + cloudResourceStatus +
                '}';
    }

    //END GENERATED CODE
}
