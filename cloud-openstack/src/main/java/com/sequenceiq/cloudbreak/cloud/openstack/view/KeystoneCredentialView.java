package com.sequenceiq.cloudbreak.cloud.openstack.view;

import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;

public class KeystoneCredentialView {
    private static final String CB_KEYPAIR_NAME = "cb-keypair-";
    public static final String CB_KEYSTONE_V2 = "cb-keystone-v2";
    public static final String CB_KEYSTONE_V3 = "cb-keystone-v3";
    public static final String CB_KEYSTONE_V3_DEFAULT_SCOPE = "cb-keystone-v3-default-scope";
    public static final String CB_KEYSTONE_V3_PROJECT_SCOPE = "cb-keystone-v3-project-scope";
    public static final String CB_KEYSTONE_V3_DOMAIN_SCOPE = "cb-keystone-v3-domain-scope";

    private CloudCredential cloudCredential;

    public KeystoneCredentialView(CloudCredential cloudCredential) {
        this.cloudCredential = cloudCredential;
    }

    public String getKeyPairName() {
        return CB_KEYPAIR_NAME + deleteWhitespace(getName().toLowerCase());
    }

    public String getName() {
        return cloudCredential.getName();
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

    public String getUserDomain() {
        return cloudCredential.getParameter("userDomain", String.class);
    }

    public String getProjectName() {
        return cloudCredential.getParameter("projectName", String.class);
    }

    public String getProjectDomain() {
        return cloudCredential.getParameter("projectDomainName", String.class);
    }

    public String getDomainName() {
        return cloudCredential.getParameter("domainName", String.class);
    }

    public String getScope() { return cloudCredential.getParameter("keystoneAuthScope", String.class); }

    public String getVersion() { return cloudCredential.getParameter("keystoneVersion", String.class); }
}
