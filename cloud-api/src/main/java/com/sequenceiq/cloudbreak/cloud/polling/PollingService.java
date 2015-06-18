package com.sequenceiq.cloudbreak.cloud.polling;

public interface PollingService<T> {

    T doPoll(T pollingInfo);

    T handleSuccess(T freshPollingInfo);

    T handleFailure(T freshPollingInfo);

    T handlePermanentPollingInfo(T pollingInfo);

}
