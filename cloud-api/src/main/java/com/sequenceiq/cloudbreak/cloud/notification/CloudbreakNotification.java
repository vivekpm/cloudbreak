package com.sequenceiq.cloudbreak.cloud.notification;

/**
 * Wrapper for objects intended to be used as events.
 * It's intended to ease the separation of various notifications (by type)
 *
 * @param <T> the type of the payload of the notification
 */
public interface CloudbreakNotification<T> {
    T payload();
}
