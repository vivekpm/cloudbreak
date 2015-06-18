package com.sequenceiq.cloudbreak.cloud.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.cloud.notification.model.PollingNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.PollingResultNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.ResourceAllocationNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.ResourceAllocationPersisted;
import com.sequenceiq.cloudbreak.cloud.polling.ResourcePollingInfo;

@Service
public class CloudReactorStateProcessor implements ReactorStateProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudReactorStateProcessor.class);

    private Map<Class, Object> sourceReferenceToSelectorMap;

    @Override
    public Object selectorData(ReactorStateContext stateContext) {
        LOGGER.info("Getting selector data from the reactor state context: {}", stateContext);
        if (!(stateContext instanceof CloudReactorStateContext)) {
            throw new IllegalStateException("Unsuported context type!" + stateContext.getClass());
        }
        if (stateContext.sourceReference() == null) {
            throw new IllegalStateException("No source reference provided! Can't determine what to do next!" + stateContext);
        }
        // next selector based on the payload type; source reference not used!!! (this won't be enough)
        return stateContext.payload().getClass().getSimpleName().toUpperCase();
    }

    @Override
    public Object payload(ReactorStateContext stateContext) {
        Object ret = null;
        // todo this method should support many payload types; the selector and the payload are related, invent something smarter here!
        // eg. : set of payload processors based on type
        LOGGER.info("Getting payload from reactor state context: {}", stateContext);
        if (stateContext.payload() instanceof ResourcePollingInfo) {
            ret = stateContext.payload();
        } else if (stateContext.payload() instanceof ResourceAllocationNotification) {
            ret = stateContext.payload();
        } else if (stateContext.payload() instanceof ResourceAllocationPersisted) {
            ResourceAllocationPersisted notification = (ResourceAllocationPersisted) stateContext.payload();
            notification.getRequest().getPromise().onNext(notification);
        } else if (stateContext.payload() instanceof PollingNotification) {
            ret = stateContext.payload();
        } else if (stateContext.payload() instanceof PollingResultNotification) {
            ret = stateContext.payload();
        } else {
            LOGGER.warn("Payload processing resulted null!!! statecontext: {}", stateContext);
        }
        return ret;
    }

}
