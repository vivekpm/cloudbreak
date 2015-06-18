package com.sequenceiq.cloudbreak.cloud.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.notification.EventBusAwareNotifier;

import reactor.bus.Event;

/**
 * Consumer for managing activities during cloud operations.
 * It's in charge for determining what's the next operation and triggers the appropriate event
 */
@Component
public class ReactorStateDispatcher implements CloudPlatformEventHandler<ReactorStateContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorStateDispatcher.class);

    @Inject
    private ReactorStateProcessor reactorStateProcessor;

    @Inject
    @Named("DispatchingNotifier")
    private EventBusAwareNotifier notifier;

    @Override
    public void accept(Event<ReactorStateContext> event) {
        Object selectorData = reactorStateProcessor.selectorData(event.getData());
        LOGGER.debug("Selector from  reactor state context: {}", selectorData);

        Object payload = reactorStateProcessor.payload(event.getData());
        LOGGER.debug("Processed payload from reactor state context: {}", selectorData);

        if (selectorData != null) {
            LOGGER.info("Dispatching new event for selector: {}", selectorData);
            notifier.getEventBus().send(selectorData, DispatchingEventFactory.createDispatchingEvent(payload, type().getSimpleName().toUpperCase()));
        } else {
            LOGGER.info("No further steps needed for: {}", payload);
        }
    }

    @Override
    public Class<ReactorStateContext> type() {
        return ReactorStateContext.class;
    }
}
