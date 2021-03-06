package com.sequenceiq.cloudbreak.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sequenceiq.cloudbreak.common.type.AzureVmType;
import com.sequenceiq.cloudbreak.common.type.CloudPlatform;

@Entity
public class AzureTemplate extends Template implements ProvisionEntity {

    @Enumerated(EnumType.STRING)
    private AzureVmType vmType;

    public AzureTemplate() {
    }

    public AzureVmType getVmType() {
        return vmType;
    }

    public void setVmType(AzureVmType vmType) {
        this.vmType = vmType;
    }

    @Override
    public CloudPlatform cloudPlatform() {
        return CloudPlatform.AZURE;
    }

    @Override
    public String getInstanceTypeName() {
        return getVmType().vmType();
    }

    @Override
    public String getVolumeTypeName() {
        return "HDD";
    }

}


