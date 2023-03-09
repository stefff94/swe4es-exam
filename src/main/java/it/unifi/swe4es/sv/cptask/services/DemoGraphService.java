package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DemoGraphService {

    private final Graph demoGraph;
    private final Supplier<Stream<Node>> demoNodesSupplier;

    public DemoGraphService() {
        Node v1 = new Node("v1", 1, NodeType.REGULAR);
        Node v2 = new Node("v2", 1, NodeType.CONDITIONAL_BEGINNING);
        Node v3 = new Node("v3", 3, NodeType.REGULAR);
        Node v4 = new Node("v4", 4, NodeType.REGULAR);
        Node v5 = new Node("v5", 2, NodeType.REGULAR);
        Node v6 = new Node("v6", 0, NodeType.CONDITIONAL_END);
        Node v7 = new Node("v7", 1, NodeType.REGULAR);
        Node v8 = new Node("v8", 1, NodeType.REGULAR);
        Node v9 = new Node("v9", 1, NodeType.REGULAR);

        demoNodesSupplier = () -> Stream.of(v1, v2, v3, v4, v5, v6, v7, v8, v9);

        demoGraph = new Graph();

        Stream<Node> nodes = getDemoNodes();

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

    public Stream<Node> getDemoNodes() {
        return this.demoNodesSupplier.get();
    }

    public Stream<Node> getDemoNodes(Node... nodes) {
        Set<Node> nodeSet = Arrays.stream(nodes)
                .collect(Collectors.toSet());

        return getDemoNodes(nodeSet);
    }

    public Stream<Node> getDemoNodes(Set<Node> nodes) {
        return this.demoNodesSupplier.get().filter(nodes::contains);
    }

    public Stream<Node> getDemoNodes(String... nodes) {
        Set<Node> nodeSet = Arrays.stream(nodes)
                .map(this::getDemoNode)
                .collect(Collectors.toSet());

        return getDemoNodes(nodeSet);
    }

    public Node getDemoNode(String label) {
        return this.demoNodesSupplier.get().filter(n -> n.getLabel().equals(label))
                .findFirst()
                .orElse(null);
    }
    public Graph getDemoGraph() {
        return demoGraph;
    }
}
