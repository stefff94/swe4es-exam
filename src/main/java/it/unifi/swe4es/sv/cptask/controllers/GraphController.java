package it.unifi.swe4es.sv.cptask.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.unifi.swe4es.sv.cptask.deserializers.GraphDeserializer;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.mappers.GraphMapper;
import it.unifi.swe4es.sv.cptask.services.DemoGraphService;
import it.unifi.swe4es.sv.cptask.services.EuleroService;
import it.unifi.swe4es.sv.cptask.services.GraphV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {
    private final GraphV2Service graphService;
    private final DemoGraphService demoGraphService;
    private final EuleroService euleroService;
    @Autowired
    public GraphController(GraphV2Service graphService,
                           DemoGraphService demoGraphService,
                           EuleroService euleroService) {
        this.graphService = graphService;
        this.demoGraphService = demoGraphService;
        this.euleroService = euleroService;
    }

    @GetMapping("{id}")
    public ResponseEntity<GraphDTO> getGraph(@PathVariable Long id) {
        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(graphService.getGraph(id));

        return ResponseEntity.ok().body(graphDTO);
    }

    @PostMapping("/new")
    public ResponseEntity<Long> newGraph(@RequestBody(required = false) GraphDTO graphDTO) {
        Long graphId;

        if (graphDTO != null) {
            graphId = graphService.insertNewGraph(graphDTO);
        } else {
            graphId = graphService.insertNewGraph(demoGraphService.getDemoGraph());
        }

        return ResponseEntity.ok().body(graphId);
    }

    @PostMapping("/new/json")
    public ResponseEntity<Long> newGraph(@RequestBody String jsonGraph) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("GraphDeserializer",
                new Version(1, 0, 0, null, null, null));

        module.addDeserializer(GraphDTO.class, new GraphDeserializer());
        mapper.registerModule(module);
        GraphDTO graphDTO = mapper.readValue(jsonGraph, GraphDTO.class);

        Long graphId = graphService.insertNewGraph(graphDTO);

        return ResponseEntity.ok()
                .body(graphId);

    }

    @GetMapping("/random")
    public ResponseEntity<Long> generateRandomGraph() {
        Long graphId = graphService
                .insertNewGraph(euleroService.generateRandomGraph());

        // graphId: 29

        return ResponseEntity.ok(graphId);
    }

    @GetMapping("/demo")
    public ResponseEntity<Long> generateDemoGraph() {
        Long graphId = graphService
                .insertNewGraph(euleroService.generateDemoGraph());

        // graphId: 184

        return ResponseEntity.ok(graphId);
    }
}
