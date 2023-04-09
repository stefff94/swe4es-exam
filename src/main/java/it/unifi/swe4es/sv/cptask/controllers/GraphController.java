package it.unifi.swe4es.sv.cptask.controllers;

import it.unifi.swe4es.sv.cptask.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {

    private final GraphService graphService;
    @Autowired
    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    /*@PostMapping("/new")
    public GraphDTO newTeammate(@RequestBody GraphDAO graph) {
        //return graphService.insertNewGraph(graph);
        return null;
    }*/
}
