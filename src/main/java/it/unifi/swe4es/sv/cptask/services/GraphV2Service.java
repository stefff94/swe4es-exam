package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.GraphV2;
import it.unifi.swe4es.sv.cptask.models.NodeV2;
import it.unifi.swe4es.sv.cptask.repositories.GraphV2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class GraphV2Service {

    private final GraphV2Repository graphV2Repository;

    private final NodeV2Service nodeV2Service;

    private final AdjListService adjListService;

    @Autowired
    public GraphV2Service(GraphV2Repository graphV2Repository, NodeV2Service nodeV2Service, AdjListService adjListService) {
        this.graphV2Repository = graphV2Repository;
        this.nodeV2Service = nodeV2Service;
        this.adjListService = adjListService;
    }

    public GraphV2 getGraph(Long id) {
        return graphV2Repository.findById(id).orElse(null);
    }

    public Long insertNewGraph(GraphDTO graphDTO) {
        GraphV2 graph = new GraphV2();
        graph.setName(graphDTO.getName());

        GraphV2 savedGraph = graphV2Repository.save(graph);

        Set<NodeDTO> nodes = graphDTO.getNodes();

        nodes.forEach(n -> nodeV2Service.insertNewNode(n, savedGraph));

        for (NodeDTO n : nodes) {
            NodeV2 nodeV2 = nodeV2Service.insertNewNode(n, savedGraph);
            List<NodeDTO> adjNodes = graphDTO.getAdjNodes(n);
            List<NodeV2> nodeV2List = adjNodes.stream()
                    .map(n2 -> nodeV2Service.insertNewNode(n2, savedGraph))
                    .collect(Collectors.toList());

            adjListService.insertAdjListForNode(nodeV2, nodeV2List);
        }

        return graph.getId();
    }

    /**
     * Function that gives a topological sort for the given graph
     *
     * @param graph: given graph
     * @return the list of nodes that compose the desired topological sort
     */
    public List<NodeDTO> topologicalSort(GraphDTO graph) {
        // initialization
        Stack<NodeDTO> stack = new Stack<>();
        graph.getNodes().forEach(n -> n.setVisited(false));

        // graph.getNodes().forEach(n -> topologicalSortUtil(graph, n, stack));
        for (NodeDTO n : graph.getNodes()) {
            if (!n.isVisited()) {
                topologicalSortUtil(graph, n, stack);
            }
        }

        List<NodeDTO> result = new ArrayList<>(graph.getNodes().size());

        while (!stack.empty()) {
            result.add(stack.pop());
        }

        return result;
    }

    /**
     * Utils recursive function for topological sort
     * @param graph: given graph
     * @param node: current node
     * @param stack: stack to push sorted nodes
     */
    private void topologicalSortUtil(GraphDTO graph, NodeDTO node, Stack<NodeDTO> stack) {
        node.setVisited(true);

        graph.getAdjNodes(node).forEach(n -> {
            if (!n.isVisited()) {
                topologicalSortUtil(graph, n, stack);
            }
        });

        stack.push(node);
    }
}
