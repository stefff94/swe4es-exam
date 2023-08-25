package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.GraphV2;
import it.unifi.swe4es.sv.cptask.models.NodeV2;
import it.unifi.swe4es.sv.cptask.repositories.GraphV2Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GraphV2Service {

    private final GraphV2Repository graphV2Repository;

    private final NodeV2Service nodeV2Service;

    private final AdjListService adjListService;

    public GraphV2Service(GraphV2Repository graphV2Repository, NodeV2Service nodeV2Service, AdjListService adjListService) {
        this.graphV2Repository = graphV2Repository;
        this.nodeV2Service = nodeV2Service;
        this.adjListService = adjListService;
    }

    public void insertNewGraph(GraphDTO graphDTO) {
        // GraphV2 savedGraph = graphV2Repository.save(graphV2);

        GraphV2 graph = new GraphV2();
        graph.setName(graphDTO.getName());

        GraphV2 savedGraph = graphV2Repository.save(graph);

        Set<NodeDTO> nodes = graphDTO.getNodes();

        /*nodes.forEach(n -> {
            NodeV2 node = new NodeV2();
            node.setGraph(savedGraph);
            node.setLabel(n.getLabel());
            node.setType(n.getType());
            node.setWeight(n.getWeight());


        });*/

        nodes.forEach(n -> nodeV2Service.insertNewNode(n, savedGraph));

        for (NodeDTO n : nodes) {
            NodeV2 nodeV2 = nodeV2Service.insertNewNode(n, savedGraph);
            List<NodeDTO> adjNodes = graphDTO.getAdjNodes(n);
            List<NodeV2> nodeV2List = adjNodes.stream()
                    .map(n2 -> nodeV2Service.insertNewNode(n2, savedGraph))
                    .collect(Collectors.toList());

            adjListService.insertAdjListForNode(nodeV2, nodeV2List);

            // adjListService.insertAdjElement(n, adjNodes)
        }

    }
}
