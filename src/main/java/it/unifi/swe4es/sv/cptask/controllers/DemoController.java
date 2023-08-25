package it.unifi.swe4es.sv.cptask.controllers;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import it.unifi.swe4es.sv.cptask.services.DemoGraphService;
import it.unifi.swe4es.sv.cptask.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

  private final GraphService graphService;
  private final DemoGraphService demoGraphService;

  @Autowired
  public DemoController(GraphService graphService, DemoGraphService demoGraphService) {
    this.graphService = graphService;
    this.demoGraphService = demoGraphService;
  }

  @GetMapping
  public List<NodeDTO> demo() {
    NodeDTO six = new NodeDTO("6", 0, NodeType.REGULAR);
    NodeDTO five = new NodeDTO("5", 0, NodeType.REGULAR);
    NodeDTO four = new NodeDTO("4", 0, NodeType.REGULAR);
    NodeDTO three = new NodeDTO("3", 0, NodeType.REGULAR);
    NodeDTO two = new NodeDTO("2", 0, NodeType.REGULAR);
    NodeDTO one = new NodeDTO("1", 0, NodeType.REGULAR);
    NodeDTO zero = new NodeDTO("0", 0, NodeType.REGULAR);

    GraphDTO graph = new GraphDTO();

    graph.addAllNodes(six, five, four, three, two, one, zero);

    graph.addDirectedArc(six, five);
    graph.addDirectedArc(six, four);
    graph.addDirectedArc(five, two);
    graph.addDirectedArc(five, zero);
    graph.addDirectedArc(four, zero);
    graph.addDirectedArc(four, one);
    graph.addDirectedArc(two, three);
    graph.addDirectedArc(three, one);

    return graphService.topologicalSort(graph);

  }

  @GetMapping("graph")
  public GraphDTO demoGraph() {
    return demoGraphService.getDemoGraph();
  }
}
