package com.sequenceiq.cloudbreak.cloud.handler;

import reactor.bus.Event;

/**
 * Factory for creating events. This utility should be used everywhere in the application
 * instead of the reactor api, to make sure events are correctly set up.
 * <p/>
 * Add more methods for customizing events (eg.: set custom error handlers)
 */
public class DispatchingEventFactory {
    private DispatchingEventFactory() {
    }

    /**
     * It sets the replyTo key to the event
     *
     * @param payload    the payload of the event
     * @param replyToKey the selector key the event consumer needs to report back
     * @return an Event to be sent to the approproate consumer
     */
    public static Event createDispatchingEvent(Object payload, Object replyToKey) {
        return Event.wrap(payload, replyToKey);
    }

    public static Event createEventWitKey(Object payload, Object key) {
        Event event = Event.wrap(payload);
        event.setKey(key);
        return event;
    }

}
