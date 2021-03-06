package com.sequenceiq.cloudbreak.cloud.openstack.view;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;

import com.sequenceiq.cloudbreak.cloud.context.AuthenticatedContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;

public class KeystoneCredentialView {
    private static final String CB_KEYPAIR_NAME = "cb";

    private CloudCredential cloudCredential;
    private String stackName;

    public KeystoneCredentialView(AuthenticatedContext authenticatedContext) {
        this.stackName = authenticatedContext.getCloudContext().getName();
        this.cloudCredential = authenticatedContext.getCloudCredential();
    }

    public String getKeyPairName() {
        return String.format("%s-%s-%s-%s", CB_KEYPAIR_NAME, getStackName(), deleteWhitespace(getName().toLowerCase()), cloudCredential.getId());
    }

    public String getName() {
        return cloudCredential.getName();
    }

    public String getStackName() {
        return stackName;
    }

    public String getPublicKey() {
        return cloudCredential.getPublicKey();
    }

    public String getUserName() {
        return cloudCredential.getParameter("userName", String.class);
    }

    public String getPassword() {
        return cloudCredential.getParameter("password", String.class);
    }

    public String getTenantName() {
        return cloudCredential.getParameter("tenantName", String.class);
    }

    public String getEndpoint() {
        return cloudCredential.getParameter("endpoint", String.class);
    }

}
