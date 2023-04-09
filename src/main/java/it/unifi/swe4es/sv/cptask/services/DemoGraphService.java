package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DemoGraphService {

    private final GraphDTO demoGraph;
    private final Supplier<Stream<NodeDTO>> demoNodesSupplier;

    public DemoGraphService() {
        NodeDTO v1 = new NodeDTO("v1", 1, NodeType.REGULAR);
        NodeDTO v2 = new NodeDTO("v2", 1, NodeType.CONDITIONAL_BEGINNING);
        NodeDTO v3 = new NodeDTO("v3", 3, NodeType.REGULAR);
        NodeDTO v4 = new NodeDTO("v4", 4, NodeType.REGULAR);
        NodeDTO v5 = new NodeDTO("v5", 2, NodeType.REGULAR);
        NodeDTO v6 = new NodeDTO("v6", 0, NodeType.CONDITIONAL_END);
        NodeDTO v7 = new NodeDTO("v7", 1, NodeType.REGULAR);
        NodeDTO v8 = new NodeDTO("v8", 1, NodeType.REGULAR);
        NodeDTO v9 = new NodeDTO("v9", 1, NodeType.REGULAR);

        demoNodesSupplier = () -> Stream.of(v1, v2, v3, v4, v5, v6, v7, v8, v9);

        demoGraph = new GraphDTO();

        Stream<NodeDTO> nodes = getDemoNodes();

        demoGraph.addAllNodes(nodes);

        demoGraph.addDirectedArc(v1, v2);
        demoGraph.addDirectedArc(v1, v5);
        demoGraph.addDirectedArc(v2, v3);
        demoGraph.addDirectedArc(v2, v4);
        demoGraph.addDirectedArc(v4, v6);
        demoGraph.addDirectedArc(v3, v6);
        demoGraph.addDirectedArc(v5, v8);
        demoGraph.addDirectedArc(v5, v7);
        demoGraph.addDirectedArc(v6, v7);
        demoGraph.addDirectedArc(v6, v8);
        demoGraph.addDirectedArc(v8, v9);
        demoGraph.addDirectedArc(v7, v9);
    }

    public Stream<NodeDTO> getDemoNodes() {
        return this.demoNodesSupplier.get();
    }

    public Stream<NodeDTO> getDemoNodes(NodeDTO... nodes) {
        Set<NodeDTO> nodeSet = Arrays.stream(nodes)
                .collect(Collectors.toSet());

        return getDemoNodes(nodeSet);
    }

    public Stream<NodeDTO> getDemoNodes(Set<NodeDTO> nodes) {
        return this.demoNodesSupplier.get().filter(nodes::contains);
    }

    public Stream<NodeDTO> getDemoNodes(String... nodes) {
        Set<NodeDTO> nodeSet = Arrays.stream(nodes)
                .map(this::getDemoNode)
                .collect(Collectors.toSet());

        return getDemoNodes(nodeSet);
    }

    public NodeDTO getDemoNode(String label) {
        return this.demoNodesSupplier.get().filter(n -> n.getLabel().equals(label))
                .findFirst()
                .orElse(null);
    }
    public GraphDTO getDemoGraph() {
        return demoGraph;
    }
}
