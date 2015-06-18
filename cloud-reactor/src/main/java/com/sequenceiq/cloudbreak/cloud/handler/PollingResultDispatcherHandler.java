package com.sequenceiq.cloudbreak.cloud.handler;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.notification.StateDispatcherNotifier;
import com.sequenceiq.cloudbreak.cloud.notification.model.PollingResultNotification;
import com.sequenceiq.cloudbreak.cloud.polling.PollingInfo;
import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;

import reactor.bus.Event;


@Component
public class PollingResultDispatcherHandler implements CloudPlatformEventHandler<PollingResultNotification> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollingResultDispatcherHandler.class);

    @Inject
    private StateDispatcherNotifier stateDispatcherNotifier;

    @Override
    public void accept(Event<PollingResultNotification> pollingInfoEvent) {

        //todo extract the logic into a service
        LOGGER.info("Polling result notification received: {}", pollingInfoEvent);
        PollingInfo pollingInfo = pollingInfoEvent.getData().payload();

        LOGGER.debug("Dispatch polling result: {}", pollingInfo);
        switch (pollingInfo.pollingStatus()) {
            case TERMINATED:
                LOGGER.debug("Polling is terminated by another process!");
                throw new IllegalStateException("Unimplemented case!");
                // TODO don't forget the break;!!!
            case IN_PROGRESS:
                LOGGER.debug("Polling is active.");
                stateDispatcherNotifier.scheduleNewPollingCycle(pollingInfo);
                break;
            case FAILED:
                LOGGER.debug("Polling is failed.");
                ((ResourcePollingInfo) pollingInfo).getPromise().onNext(pollingInfo);
                break;
            case CREATED:
                LOGGER.debug("Polling success");
                ((ResourcePollingInfo) pollingInfo).getPromise().onNext(pollingInfo);
                break;
            default:
                throw new IllegalStateException("Polling in unsupported state!");
        }
    }

    @Override
    public Class<PollingResultNotification> type() {
        return PollingResultNotification.class;
    }
}
