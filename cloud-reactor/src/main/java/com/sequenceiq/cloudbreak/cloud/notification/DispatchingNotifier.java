package com.sequenceiq.cloudbreak.cloud.notification;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.handler.ReactorStateContext;

/**
 * Used only for getting the bus reference!
 */
@Component("DispatchingNotifier")
public class DispatchingNotifier extends EventBusAwareNotifier<ReactorStateContext> {
    @Override
    public void notify(ReactorStateContext payload) {
    }
}
