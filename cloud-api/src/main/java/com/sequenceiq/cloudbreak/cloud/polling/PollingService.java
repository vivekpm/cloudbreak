package com.sequenceiq.cloudbreak.cloud.polling;

/**
 * Contract for polling operations.
 *
 * @param <T> the type holding polling information
 */
public interface PollingService<T> {

    T doPoll(T pollingInfo);

    T handleSuccess(T pollingInfo);

    T handleFailure(T pollingInfo);

    T handlePermanentPollingInfo(T pollingInfo);

}
