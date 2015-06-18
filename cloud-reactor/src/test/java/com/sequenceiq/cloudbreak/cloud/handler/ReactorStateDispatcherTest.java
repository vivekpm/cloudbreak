package com.sequenceiq.cloudbreak.cloud.handler;

import static reactor.bus.selector.Selectors.$;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sequenceiq.cloudbreak.reactor.config.EventBusConfig;

import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class, EventBusConfig.class })
public class ReactorStateDispatcherTest {

    public static final int SLEEP_MILLIS = 10;
    @Inject
    private EventBus eventBus;


    @Before
    public void setUp() throws Exception {
        eventBus.on($("test"), new TestConsumer());
    }

    @Test
    public void testName() throws Exception {
        eventBus.send("test", createEvent("hello"));
        Thread.sleep(SLEEP_MILLIS);
    }

    private Event createEvent(Object payload) {
        return Event.wrap(payload, "test");
    }


    private static class TestConsumer implements Consumer<Event<String>> {
        private static final Logger LOGGER = LoggerFactory.getLogger(TestConsumer.class);

        @Override
        public void accept(Event<String> stringEvent) {
            LOGGER.info("event: {}", stringEvent);
            ((EventBus.ReplyToEvent) stringEvent).getReplyToObservable().notify(stringEvent.getKey(), stringEvent);
        }
    }
}