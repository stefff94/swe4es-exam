package it.unifi.swe4es.sv.cptask.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.mappers.GraphMapper;
import it.unifi.swe4es.sv.cptask.services.DemoGraphService;
import it.unifi.swe4es.sv.cptask.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {

    private final GraphService graphService;
    private final DemoGraphService demoGraphService;
    @Autowired
    public GraphController(GraphService graphService, DemoGraphService demoGraphService) {
        this.graphService = graphService;
        this.demoGraphService = demoGraphService;
    }

    /*@PostMapping("/new-node")
    public NodeDTO newNode(@RequestBody Node node) {
        return NodeMapper.INSTANCE.toDTO(graphService.insertNewNode(node));
    }*/

    @GetMapping("{id}")
    public ResponseEntity<GraphDTO> getGraph(@PathVariable Long id) {
        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(graphService.getGraph(id));

        return ResponseEntity.ok().body(graphDTO);
    }

    /*@PostMapping("/new")
    public ResponseEntity<GraphDTO> newGraph(@RequestBody GraphDTO graph) {
        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(
                graphService.insertNewGraph(GraphMapper.INSTANCE.fromDTO(graph)));

        return ResponseEntity.ok().body(graphDTO);

    }*/

    @PostMapping("/new")
    public ResponseEntity<GraphDTO> newGraph(@RequestBody String graphJson) throws JsonProcessingException {
        /*GraphDTO graph = new ObjectMapper().readValue(graphJson, GraphDTO.class);

        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(
                graphService.insertNewGraph(GraphMapper.INSTANCE.fromDTO(graph)));*/

        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(
                graphService.insertNewGraph(GraphMapper.INSTANCE.fromDTO(demoGraphService.getDemoGraph())));

        return ResponseEntity.ok().body(graphDTO);

    }
}
