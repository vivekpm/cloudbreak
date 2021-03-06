package com.sequenceiq.cloudbreak.orchestrator;

import java.util.List;
import java.util.Set;

import com.sequenceiq.cloudbreak.orchestrator.exception.CloudbreakOrchestratorException;
import com.sequenceiq.cloudbreak.orchestrator.executor.ParallelContainerRunner;
import com.sequenceiq.cloudbreak.orchestrator.model.GatewayConfig;
import com.sequenceiq.cloudbreak.orchestrator.model.LogVolumePath;
import com.sequenceiq.cloudbreak.orchestrator.model.Node;
import com.sequenceiq.cloudbreak.orchestrator.security.KerberosConfiguration;
import com.sequenceiq.cloudbreak.orchestrator.state.ExitCriteria;
import com.sequenceiq.cloudbreak.orchestrator.state.ExitCriteriaModel;

public interface ContainerOrchestrator {

    String name();

    void init(ParallelContainerRunner parallelContainerRunner, ExitCriteria exitCriteria);

    void bootstrap(GatewayConfig gatewayConfig, Set<Node> nodes, int consulServerCount, String consulLogLocation, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void bootstrapNewNodes(GatewayConfig gatewayConfig, Set<Node> nodes, String consulLogLocation, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startRegistrator(ContainerOrchestratorCluster cluster, String imageName, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startAmbariServer(ContainerOrchestratorCluster cluster, String dbImageName, String serverImageName, String platform,
            LogVolumePath logVolumePath, Boolean localAgentRequired, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startAmbariAgents(ContainerOrchestratorCluster cluster, String imageName, String platform, LogVolumePath logVolumePath,
            ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startConsulWatches(ContainerOrchestratorCluster cluster, String imageName, LogVolumePath logVolumePath, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startKerberosServer(ContainerOrchestratorCluster cluster, String serverImageName, LogVolumePath logVolumePath,
            KerberosConfiguration kerberosConfiguration, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startBaywatchServer(ContainerOrchestratorCluster cluster, String imageName, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startBaywatchClients(ContainerOrchestratorCluster cluster, String imageName, String consulDomain, LogVolumePath logVolumePath,
            String externServerLocation, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    void startLogrotate(ContainerOrchestratorCluster cluster, String imageName, ExitCriteriaModel exitCriteriaModel)
            throws CloudbreakOrchestratorException;

    boolean areAllNodesAvailable(GatewayConfig gatewayConfig, Set<Node> nodes);

    List<String> getAvailableNodes(GatewayConfig gatewayConfig, Set<Node> nodes);

    boolean isBootstrapApiAvailable(GatewayConfig gatewayConfig);

    int getMaxBootstrapNodes();

}
