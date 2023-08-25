package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.models.AdjList;
import it.unifi.swe4es.sv.cptask.models.NodeV2;
import it.unifi.swe4es.sv.cptask.repositories.AdjListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdjListService {

    private final AdjListRepository adjListRepository;

    public AdjListService(AdjListRepository adjListRepository) {
        this.adjListRepository = adjListRepository;
    }

    public void insertAdjListForNode(NodeV2 from, List<NodeV2> adjNodes) {
        adjNodes.forEach(to -> insertAdjList(from, to));
    }

    public void insertAdjList(NodeV2 from, NodeV2 to) {
        AdjList adjList = new AdjList();
        adjList.setFrom(from);
        adjList.setTo(to);

        adjListRepository.save(adjList);
    }
}
