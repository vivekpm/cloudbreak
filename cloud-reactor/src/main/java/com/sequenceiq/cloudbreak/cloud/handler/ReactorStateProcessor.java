package com.sequenceiq.cloudbreak.cloud.handler;

public interface ReactorStateProcessor {
    /**
     * Processes the key of the next event/action if any
     *
     * @param stateContext the context holding the necessary information for processing the next event
     * @return a the payload for the selector of the next event
     */
    Object selectorData(ReactorStateContext stateContext);

    /**
     * Processes the payload for the next event
     *
     * @param stateContext the context holding the necessary information for processing the next event
     * @return the payload for the next event
     */
    Object payload(ReactorStateContext stateContext);
}
