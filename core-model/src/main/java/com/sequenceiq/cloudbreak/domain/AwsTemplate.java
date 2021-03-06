package com.sequenceiq.cloudbreak.domain;

import static com.sequenceiq.cloudbreak.common.type.AwsVolumeType.Ephemeral;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sequenceiq.cloudbreak.common.type.AwsEncryption;
import com.sequenceiq.cloudbreak.common.type.AwsInstanceType;
import com.sequenceiq.cloudbreak.common.type.AwsVolumeType;
import com.sequenceiq.cloudbreak.common.type.CloudPlatform;

@Entity
public class AwsTemplate extends Template implements ProvisionEntity {

    @Enumerated(EnumType.STRING)
    private AwsInstanceType instanceType;
    private String sshLocation;
    @Enumerated(EnumType.STRING)
    private AwsVolumeType volumeType;
    private Double spotPrice;
    @Enumerated(EnumType.STRING)
    private AwsEncryption encrypted;

    public AwsTemplate() {
    }

    public AwsInstanceType getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(AwsInstanceType instanceType) {
        this.instanceType = instanceType;
    }

    public String getSshLocation() {
        return sshLocation;
    }

    public void setSshLocation(String sshLocation) {
        this.sshLocation = sshLocation;
    }

    @Override
    public CloudPlatform cloudPlatform() {
        return CloudPlatform.AWS;
    }

    @Override
    public String getInstanceTypeName() {
        return getInstanceType().getValue();
    }

    @Override
    public String getVolumeTypeName() {
        return getVolumeType().getValue();
    }

    public AwsVolumeType getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(AwsVolumeType volumeType) {
        this.volumeType = volumeType;
    }

    public Double getSpotPrice() {
        return spotPrice;
    }

    public void setSpotPrice(Double spotPrice) {
        this.spotPrice = spotPrice;
    }

    public AwsEncryption getEncrypted() {
        return encrypted;
    }

    public boolean isEncrypted() {
        return AwsEncryption.TRUE.equals(encrypted) ? true : false;
    }

    public void setEncrypted(AwsEncryption encrypted) {
        this.encrypted = encrypted;
    }

    public boolean isEphemeral() {
        return volumeType.equals(Ephemeral);
    }
}
