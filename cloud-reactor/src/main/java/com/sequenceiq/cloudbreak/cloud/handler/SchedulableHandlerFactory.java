package com.sequenceiq.cloudbreak.cloud.handler;

import com.sequenceiq.cloudbreak.cloud.notification.StateDispatcherNotifier;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;

import reactor.fn.Consumer;

public class SchedulableHandlerFactory {
    private SchedulableHandlerFactory() {
    }

    public static Consumer<Long> createSchedulablePollingHandler(PollingInfo pollingInfo, StateDispatcherNotifier stateDispatcherNotifier) {
        return new SchedulablePollingHandler(pollingInfo, stateDispatcherNotifier);
    }
}
