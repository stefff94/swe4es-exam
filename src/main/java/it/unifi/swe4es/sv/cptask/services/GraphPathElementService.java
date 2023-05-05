package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.models.GraphPathElement;
import it.unifi.swe4es.sv.cptask.repositories.GraphPathElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraphPathElementService {

    private final GraphPathElementRepository graphPathElementRepository;

    @Autowired
    public GraphPathElementService(GraphPathElementRepository graphPathElementRepository) {
        this.graphPathElementRepository = graphPathElementRepository;
    }

    public void insertNewGraphPathElement(GraphPathElement graphPathElement) {
        graphPathElementRepository.save(graphPathElement);
    }
}
