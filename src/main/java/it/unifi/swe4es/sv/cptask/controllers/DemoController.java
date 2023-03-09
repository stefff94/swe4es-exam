package it.unifi.swe4es.sv.cptask.controllers;

import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import it.unifi.swe4es.sv.cptask.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

  private final GraphService graphService;

  @Autowired
  public DemoController(GraphService graphService) {
    this.graphService = graphService;
  }

  @GetMapping
  public List<Node> demo() {
    Node six = new Node("6", 0, NodeType.REGULAR);
    Node five = new Node("5", 0, NodeType.REGULAR);
    Node four = new Node("4", 0, NodeType.REGULAR);
    Node three = new Node("3", 0, NodeType.REGULAR);
    Node two = new Node("2", 0, NodeType.REGULAR);
    Node one = new Node("1", 0, NodeType.REGULAR);
    Node zero = new Node("0", 0, NodeType.REGULAR);

    Graph graph = new Graph();

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
}
