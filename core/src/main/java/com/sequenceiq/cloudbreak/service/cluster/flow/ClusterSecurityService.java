package com.sequenceiq.cloudbreak.service.cluster.flow;

import static com.sequenceiq.cloudbreak.orchestrator.security.KerberosConfiguration.DOMAIN_REALM;
import static com.sequenceiq.cloudbreak.orchestrator.security.KerberosConfiguration.REALM;
import static com.sequenceiq.cloudbreak.service.PollingResult.isExited;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.ambari.client.AmbariClient;
import com.sequenceiq.cloudbreak.core.CloudbreakException;
import com.sequenceiq.cloudbreak.domain.Cluster;
import com.sequenceiq.cloudbreak.domain.InstanceGroup;
import com.sequenceiq.cloudbreak.domain.InstanceMetaData;
import com.sequenceiq.cloudbreak.domain.Stack;
import com.sequenceiq.cloudbreak.repository.InstanceMetaDataRepository;
import com.sequenceiq.cloudbreak.service.PollingResult;
import com.sequenceiq.cloudbreak.service.TlsSecurityService;
import com.sequenceiq.cloudbreak.service.cluster.AmbariClientProvider;
import com.sequenceiq.cloudbreak.service.events.CloudbreakEventService;
import com.sequenceiq.cloudbreak.service.stack.flow.TLSClientConfig;
import com.sequenceiq.cloudbreak.util.AmbariClientExceptionUtil;

import groovyx.net.http.HttpResponseException;

@Service
public class ClusterSecurityService {

    public static final String KERBEROS_CLIENT = "KERBEROS_CLIENT";
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterSecurityService.class);
    private static final String KERBEROS_SERVICE = "KERBEROS";

    @Inject
    private AmbariClientProvider ambariClientProvider;
    @Inject
    private AmbariOperationService ambariOperationService;
    @Inject
    private CloudbreakEventService eventService;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private TlsSecurityService tlsSecurityService;
    @Inject
    private InstanceMetaDataRepository instanceMetadataRepository;

    public void enableKerberosSecurity(Stack stack) throws CloudbreakException {
        try {
            Cluster cluster = stack.getCluster();
            TLSClientConfig clientConfig = tlsSecurityService.buildTLSClientConfig(stack.getId(), cluster.getAmbariIp());
            AmbariClient ambariClient = ambariClientProvider.getSecureAmbariClient(clientConfig, cluster);
            ambariClient.addService(KERBEROS_SERVICE);
            ambariClient.addServiceComponent(KERBEROS_SERVICE, KERBEROS_CLIENT);
            ambariClient.addComponentsToHosts(ambariClient.getClusterHosts(), asList(KERBEROS_CLIENT));
            InstanceGroup gateway = stack.getGatewayInstanceGroup();
            InstanceMetaData metaData = new ArrayList<>(gateway.getInstanceMetaData()).get(0);
            String kdcHost = metaData.getDiscoveryFQDN();
            ambariClient.createKerberosConfig(kdcHost, REALM, DOMAIN_REALM + ",." + DOMAIN_REALM);
            ambariClientProvider.setKerberosSession(ambariClient, cluster);
            int installReqId = ambariClient.setServiceState(KERBEROS_SERVICE, "INSTALLED");
            PollingResult pollingResult = waitForOperation(stack, ambariClient, singletonMap("INSTALL_KERBEROS", installReqId));
            if (isContinue(pollingResult)) {
                ambariClient.createKerberosDescriptor(REALM);
                pollingResult = waitForOperation(stack, ambariClient, singletonMap("STOP_SERVICES", ambariClient.stopAllServices()));
                if (isContinue(pollingResult)) {
                    pollingResult = waitForOperation(stack, ambariClient, singletonMap("ENABLE_KERBEROS", ambariClient.enableKerberos()));
                    if (isContinue(pollingResult)) {
                        waitForOperation(stack, ambariClient, singletonMap("START_SERVICES", ambariClient.startAllServices()));
                    }
                }
            }
        } catch (InterruptedException ie) {
            throw new CloudbreakException(ie);
        } catch (HttpResponseException hre) {
            String errorMessage = AmbariClientExceptionUtil.getErrorMessage(hre);
            LOGGER.error("Ambari could not enable Kerberos service. " + errorMessage, hre);
            throw new CloudbreakException(hre);
        } catch (Exception e) {
            LOGGER.error("Error occurred during enabling the kerberos security", e);
            throw new CloudbreakException(e);
        }
    }

    private boolean isContinue(PollingResult result) throws InterruptedException {
        if (isExited(result)) {
            throw new InterruptedException("Interrupt enabling kerberos flow");
        }
        return true;
    }

    private PollingResult waitForOperation(Stack stack, AmbariClient ambariClient, Map<String, Integer> requests) {
        return ambariOperationService.waitForAmbariOperations(stack, ambariClient, requests);
    }

}
