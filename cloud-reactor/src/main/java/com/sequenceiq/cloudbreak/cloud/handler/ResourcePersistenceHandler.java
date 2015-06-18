package com.sequenceiq.cloudbreak.cloud.handler;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.notification.model.ResourceAllocationNotification;
import com.sequenceiq.cloudbreak.cloud.notification.model.ResourceAllocationPersisted;
import com.sequenceiq.cloudbreak.cloud.service.Persister;

@Component
public class ResourcePersistenceHandler extends DispatchingEventHandler<ResourceAllocationNotification> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcePersistenceHandler.class);

    @Inject
    private Persister<ResourceAllocationNotification> cloudResourcePersisterService;

    @Override
    protected ReactorStateContext process(ResourceAllocationNotification payload) {
        LOGGER.info("ResourceAllocationNotification received: {}", payload);
        payload = cloudResourcePersisterService.persist(payload);
        return ReactorStateContextFactory.createReactorStateContext(getClass(), new ResourceAllocationPersisted(payload));
    }

    @Override
    public Class<ResourceAllocationNotification> type() {
        return ResourceAllocationNotification.class;
    }
}
