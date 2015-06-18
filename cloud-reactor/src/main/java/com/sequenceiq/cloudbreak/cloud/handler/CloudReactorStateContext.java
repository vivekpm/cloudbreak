package com.sequenceiq.cloudbreak.cloud.handler;

/**
 * Immutable context for carrying information to the state dispatcher consumer.
 */
public class CloudReactorStateContext implements ReactorStateContext {

    // identifies the handler the event is coming from
    private Object sourceReference;

    //the payload carried by the context
    private Object payload;

    public CloudReactorStateContext(Object sourceReference, Object payload) {
        this.sourceReference = sourceReference;
        this.payload = payload;
    }

    public Object sourceReference() {
        return sourceReference;
    }

    public Object payload() {
        return payload;
    }

    //BEGIN GENERATED CODE

    @Override
    public String toString() {
        return "CloudReactorStateContext{" +
                "sourceReference=" + sourceReference +
                ", payload=" + payload +
                '}';
    }
    //BEGIN GENERATED CODE

}
