package com.sequenceiq.cloudbreak.cloud.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * Event handler that reports back to a given handler based on the information from the received event.
 * Note: please see the Bus api for further improvements; by using Functions and the send API; reporting back to a
 * consumer is done automatically by the reactor framework!
 *
 * @param <T> the type of the payload
 */
public abstract class DispatchingEventHandler<T> implements CloudPlatformEventHandler<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatchingEventHandler.class);

    @Override
    public void accept(Event<T> event) {
        LOGGER.info("Accepted event: {}", event);
        ReactorStateContext payload = process(event.getData());

        LOGGER.debug("Dispatching payload: {}", payload);
        dispatch(event, payload);
    }

    /**
     * Placeholder method for the consumer logic
     *
     * @param payload the payload from the consumer's event
     * @return the result of the processing
     */
    protected abstract ReactorStateContext process(T payload);

    private void dispatch(Event<T> event, ReactorStateContext replyToPayload) {

        if (!(event instanceof EventBus.ReplyToEvent)) {
            LOGGER.error("Unsupported event type for the dispatching event consumer! event type: {}, consumer: {}", event.getClass(), getClass());
            throw new IllegalStateException("Unsupported event for this consumer. Event type: " + event.getData().getClass());
        }

        if (event.getReplyTo() == null) {
            LOGGER.error("ReplyTo key must be set in the event!");
            throw new IllegalStateException("Reply to key must be set in the event!");
        }

        EventBus.ReplyToEvent replyToEvent = (EventBus.ReplyToEvent) event;
        replyToEvent.getReplyToObservable().notify(replyToEvent.getReplyTo(), Event.wrap(replyToPayload));
    }

}
