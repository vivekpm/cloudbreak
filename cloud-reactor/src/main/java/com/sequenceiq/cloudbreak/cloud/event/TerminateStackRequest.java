package com.sequenceiq.cloudbreak.cloud.event;

import java.util.ArrayList;
import java.util.List;

import com.sequenceiq.cloudbreak.cloud.event.context.CloudContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;
import com.sequenceiq.cloudbreak.cloud.model.CloudResource;

import reactor.rx.Promise;

public class TerminateStackRequest<T> extends CloudPlatformRequest<T> {

    private CloudCredential cloudCredential;

    private List<CloudResource> cloudResources = new ArrayList<>();

    public TerminateStackRequest(CloudContext cloudContext, CloudCredential cloudCredential, Promise<T> result) {
        super(cloudContext, result);
        this.cloudCredential = cloudCredential;
    }

    public CloudCredential getCloudCredential() {
        return cloudCredential;
    }

    public List<CloudResource> getCloudResources() {
        return cloudResources;
    }

    //BEGIN GENERATED CODE

    @Override
    public String toString() {
        return "TerminateStackRequest{" +
                "cloudCredential=" + cloudCredential +
                ", cloudResources=" + cloudResources +
                '}';
    }

    //END GENERATED CODE
}