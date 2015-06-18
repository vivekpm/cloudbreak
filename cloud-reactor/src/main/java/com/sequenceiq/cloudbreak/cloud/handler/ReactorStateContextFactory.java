package com.sequenceiq.cloudbreak.cloud.handler;

public class ReactorStateContextFactory {
    private ReactorStateContextFactory() {
    }

    public static CloudReactorStateContext createReactorStateContext(Object sourceReference, Object payload) {
        return new CloudReactorStateContext(sourceReference, payload);
    }
}
