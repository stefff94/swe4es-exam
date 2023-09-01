package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.repositories.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class GraphService {

    private final GraphRepository graphRepository;

    private final NodeService nodeService;

    private final AdjListService adjListService;

    @Autowired
    public GraphService(GraphRepository graphRepository, NodeService nodeService, AdjListService adjListService) {
        this.graphRepository = graphRepository;
        this.nodeService = nodeService;
        this.adjListService = adjListService;
    }

    public Graph getGraph(Long id) {
        return graphRepository.findById(id).orElse(null);
    }

    public Long insertNewGraph(GraphDTO graphDTO) {
        Graph graph = new Graph();
        graph.setName(graphDTO.getName());

        Graph savedGraph = graphRepository.save(graph);

        Set<NodeDTO> nodes = graphDTO.getNodes();

        nodes.forEach(n -> nodeService.insertNewNode(n, savedGraph));

        for (NodeDTO n : nodes) {
            Node node = nodeService.insertNewNode(n, savedGraph);
            List<NodeDTO> adjNodes = graphDTO.getAdjNodes(n);
            List<Node> nodeList = adjNodes.stream()
                    .map(n2 -> nodeService.insertNewNode(n2, savedGraph))
                    .collect(Collectors.toList());

            adjListService.insertAdjListForNode(node, nodeList);
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
        for (NodeDTO n : graph.getNodes(true)) {
            if (!n.isVisited()) {
                topologicalSortUtil(graph, n, stack);
            }
        }

        List<NodeDTO> result = new ArrayList<>(graph.getNodes(true).size());

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
