package com.sequenceiq.cloudbreak.cloud.handler;

/**
 * Marks an object as input to the reactor state dispatcher.
 */
public interface ReactorStateContext {

    Object sourceReference();

    Object payload();

}
