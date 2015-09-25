package com.sequenceiq.cloudbreak.cloud.arm.task;

import static com.sequenceiq.cloudbreak.cloud.arm.ArmUtils.NOT_FOUND;

import java.util.Map;

import com.sequenceiq.cloud.azure.client.AzureRMClient;
import com.sequenceiq.cloudbreak.cloud.arm.ArmClient;
import com.sequenceiq.cloudbreak.cloud.arm.context.ResourceGroupCheckerContext;
import com.sequenceiq.cloudbreak.cloud.event.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.exception.CloudConnectorException;
import com.sequenceiq.cloudbreak.cloud.task.PollBooleanStateTask;

import groovyx.net.http.HttpResponseException;

public class ArmResourceGroupDeleteStatusCheckerTask extends PollBooleanStateTask {

    private ResourceGroupCheckerContext resourceGroupDeleteCheckerContext;
    private ArmClient armClient;

    public ArmResourceGroupDeleteStatusCheckerTask(AuthenticatedContext authenticatedContext, ArmClient armClient, ResourceGroupCheckerContext
            resourceGroupDeleteCheckerContext) {
        super(authenticatedContext, false);
        this.armClient = armClient;
        this.resourceGroupDeleteCheckerContext = resourceGroupDeleteCheckerContext;
    }

    @Override
    public Boolean call() {
        AzureRMClient client = armClient.createAccess(resourceGroupDeleteCheckerContext.getArmCredentialView());
        try {
            Map<String, Object> resourceGroup = client.getResourceGroup(resourceGroupDeleteCheckerContext.getGroupName());
        } catch (HttpResponseException e) {
            if (e.getStatusCode() != NOT_FOUND) {
                throw new CloudConnectorException(e.getResponse().getData().toString());
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }
}
