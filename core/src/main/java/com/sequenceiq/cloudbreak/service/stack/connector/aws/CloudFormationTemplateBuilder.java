package com.sequenceiq.cloudbreak.service.stack.connector.aws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.annotations.VisibleForTesting;
import com.sequenceiq.cloudbreak.domain.Stack;
import com.sequenceiq.cloudbreak.service.network.NetworkUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class CloudFormationTemplateBuilder {

    @Autowired
    private Configuration freemarkerConfiguration;

    public String build(Stack stack, String snapshotId, boolean existingVPC, String templatePath) {
        Map<String, Object> model = new HashMap<>();
        model.put("instanceGroups", stack.getInstanceGroupsAsList());
        model.put("existingVPC", existingVPC);
        model.put("subnets", stack.getAllowedSubnets());
        model.put("ports", NetworkUtils.getPorts(stack));
        model.put("cbSubnet", stack.getNetwork().getSubnetCIDR());
        if (snapshotId != null) {
            model.put("snapshotId", snapshotId);
        }
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerConfiguration.getTemplate(templatePath, "UTF-8"), model);
        } catch (IOException | TemplateException e) {
            throw new AwsResourceException("Failed to process CloudFormation freemarker template", e);
        }
    }

    @VisibleForTesting
    void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

}