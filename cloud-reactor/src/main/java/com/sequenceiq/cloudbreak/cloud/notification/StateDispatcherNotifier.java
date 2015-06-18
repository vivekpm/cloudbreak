package com.sequenceiq.cloudbreak.cloud.notification;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.handler.ReactorStateContext;
import com.sequenceiq.cloudbreak.cloud.handler.ReactorStateContextFactory;
import com.sequenceiq.cloudbreak.cloud.handler.SchedulableHandlerFactory;
import com.sequenceiq.cloudbreak.cloud.notification.model.PollingNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.ResourceAllocationNotification;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;

import reactor.bus.Event;
import reactor.fn.Pausable;
import reactor.fn.timer.Timer;


@Component("StateDispatcherNotifier")
public class StateDispatcherNotifier extends EventBusAwareNotifier<CloudbreakNotification> implements PersistenceNotifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateDispatcherNotifier.class);

    @Value("${cloudbreak.polling.delay.sec:10}")
    private long delay;

    @Inject
    private Timer timer;

    public Pausable scheduleNewPollingCycle(PollingInfo pollingInfo) {
        LOGGER.debug("Scheduling new  polling cycle: {}", pollingInfo);
        Pausable pausable = timer.submit(SchedulableHandlerFactory.createSchedulablePollingHandler(pollingInfo, this), delay, TimeUnit.SECONDS);
        LOGGER.debug("New polling cycle scheduled. Pausable reference: {}", pausable);
        return pausable;
    }

    public Pausable startPolling(PollingInfo pollingInfo) {
        LOGGER.debug("Starting to poll: {}", pollingInfo);
        Pausable pausable = timer.submit(SchedulableHandlerFactory.createSchedulablePollingHandler(pollingInfo, this));
        LOGGER.debug("Polling started. Pausable reference: {}", pausable);
        return pausable;
    }

    public void pollingInfoPersisted(PollingInfo pollingInfo) {
        LOGGER.debug("Notifying persisted polling info ready: {}", pollingInfo);
        notifyStateDispatcher(pollingInfo);
    }

    public void triggerPolling(PollingNotification pollingNotification) {
        LOGGER.debug("Notifying persisted polling info ready: {}", pollingNotification);
        notifyStateDispatcher(pollingNotification);
    }

    @Override
    public void notify(CloudbreakNotification event) {
        LOGGER.debug("Do nothing");
    }

    @Override
    public void notifyResourceAllocation(ResourceAllocationNotification resourceAllocationNotification) {
        notifyStateDispatcher(resourceAllocationNotification);
    }

    private void notifyStateDispatcher(Object payload) {
        getEventBus().notify(ReactorStateContext.class.getSimpleName().toUpperCase(),
                Event.wrap(ReactorStateContextFactory.createReactorStateContext(getClass(), payload)));
    }

}
