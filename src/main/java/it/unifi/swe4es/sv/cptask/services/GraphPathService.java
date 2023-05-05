package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.models.GraphPath;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.repositories.GraphPathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GraphPathService {

    private final GraphPathRepository graphPathRepository;

    private final NodeService nodeService;

    private final GraphPathElementService graphPathElementService;

    @Autowired
    public GraphPathService(GraphPathRepository graphPathRepository, NodeService nodeService, GraphPathElementService graphPathElementService) {
        this.graphPathRepository = graphPathRepository;
        this.nodeService = nodeService;
        this.graphPathElementService = graphPathElementService;
    }

    public void insertNewGraphPath(GraphPath graphPath) {
        Node savedNode = nodeService.insertNewNode(graphPath.getNode());

        graphPath.setNode(savedNode);

        GraphPath savedGraphPath = graphPathRepository.save(graphPath);

        graphPath.getElements().forEach(gpe -> gpe.setGraphPath(savedGraphPath));
        graphPath.getElements().forEach(gpe -> gpe.setNode(savedNode));

        graphPath.getElements().forEach(graphPathElementService::insertNewGraphPathElement);

    }
}
