package com.sequenceiq.cloudbreak.cloud.handler;

import reactor.bus.Event;
import reactor.fn.Consumer;

/**
 * Marker for cloud platform event consumers
 *
 * @param <T> the type of the data to be consumed by implementers.
 */
public interface CloudPlatformEventHandler<T> extends Consumer<Event<T>> {

    Class<T> type();

}
