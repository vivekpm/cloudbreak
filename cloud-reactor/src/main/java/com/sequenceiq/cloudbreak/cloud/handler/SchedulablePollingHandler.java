package com.sequenceiq.cloudbreak.cloud.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sequenceiq.cloudbreak.cloud.notification.StateDispatcherNotifier;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;

import reactor.fn.Consumer;

/**
 * Entry point for the polling mechanism.
 * Intended to be used with the Timer API.
 */
public class SchedulablePollingHandler implements Consumer<Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulablePollingHandler.class);

    private PollingInfo pollingInfo;
    private StateDispatcherNotifier stateDispatcherNotifier;

    public SchedulablePollingHandler(PollingInfo pollingInfo, StateDispatcherNotifier stateDispatcherNotifier) {
        this.pollingInfo = pollingInfo;
        this.stateDispatcherNotifier = stateDispatcherNotifier;
    }

    @Override
    public void accept(Long id) {
        stateDispatcherNotifier.triggerPolling(NotificationFactory.createPollingNotification(pollingInfo));
    }
}
