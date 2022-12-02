package it.unifi.swe4es.sv.cptask.controllers;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.mappers.NodeMapper;
import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import it.unifi.swe4es.sv.cptask.services.CpTaskService;
import it.unifi.swe4es.sv.cptask.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/cp-task")
public class CpTaskController {

  private final GraphService graphService;
  private final CpTaskService cpTaskService;

  @Autowired
  public CpTaskController(GraphService graphService, CpTaskService cpTaskService) {
    this.graphService = graphService;
    this.cpTaskService = cpTaskService;
  }

  @GetMapping("/demo")
  public List<NodeDTO> demo() {
    Node v1 = new Node("v1", 1, NodeType.REGULAR);
    Node v2 = new Node("v2", 1, NodeType.CONDITIONAL_BEGINNING);
    Node v3 = new Node("v3", 3, NodeType.REGULAR);
    Node v4 = new Node("v4", 4, NodeType.REGULAR);
    Node v5 = new Node("v5", 2, NodeType.REGULAR);
    Node v6 = new Node("v6", 0, NodeType.CONDITIONAL_END);
    Node v7 = new Node("v7", 1, NodeType.REGULAR);
    Node v8 = new Node("v8", 1, NodeType.REGULAR);
    Node v9 = new Node("v9", 1, NodeType.REGULAR);

    Graph graph = new Graph();

    graph.addAllNode(v1, v2, v3, v4, v5, v6, v7, v8, v9);

    graph.addDirectedArc(v1, v2);
    graph.addDirectedArc(v1, v5);
    graph.addDirectedArc(v2, v3);
    graph.addDirectedArc(v2, v4);
    graph.addDirectedArc(v4, v6);
    graph.addDirectedArc(v3, v6);
    graph.addDirectedArc(v5, v8);
    graph.addDirectedArc(v5, v7);
    graph.addDirectedArc(v6, v7);
    graph.addDirectedArc(v6, v8);
    graph.addDirectedArc(v8, v9);
    graph.addDirectedArc(v7, v9);

    return graphService.topologicalSort(graph).stream()
            .map(NodeMapper.INSTANCE::toDTO)
            .collect(Collectors.toList());
  }

  @GetMapping("/demo2")
  public NodeDTO demo2() {
    Node v1 = new Node("v1", 1, NodeType.REGULAR);
    Node v2 = new Node("v2", 1, NodeType.CONDITIONAL_BEGINNING);
    Node v3 = new Node("v3", 3, NodeType.REGULAR);
    Node v4 = new Node("v4", 4, NodeType.REGULAR);
    Node v5 = new Node("v5", 2, NodeType.REGULAR);
    Node v6 = new Node("v6", 0, NodeType.CONDITIONAL_END);
    Node v7 = new Node("v7", 1, NodeType.REGULAR);
    Node v8 = new Node("v8", 1, NodeType.REGULAR);
    Node v9 = new Node("v9", 10, NodeType.REGULAR);

    List<Node> nodes = Stream.of(v1, v2, v3).collect(Collectors.toList());

    Set<Node> set1 = Stream.of(v1, v2, v3).collect(Collectors.toCollection(HashSet::new));
    Set<Node> set2 = Stream.of(v4, v5, v6).collect(Collectors.toCollection(HashSet::new));
    Set<Node> set3 = Stream.of(v7, v8, v9).collect(Collectors.toCollection(HashSet::new));

    Map<Node, Set<Node>> setMap = new HashMap<>() {{
      put(v1, set1);
      put(v2, set2);
      put(v3, set3);
    }};

    return nodes.stream()
            .max(Comparator.comparing(n -> cpTaskService.computeWCET(setMap.get(n))))
            .map(NodeMapper.INSTANCE::toDTO)
            .orElse(null);
  }

  @GetMapping("/demo3")
  public Integer demo3() {
    Node v1 = new Node("v1", 1, NodeType.REGULAR);
    Node v2 = new Node("v2", 1, NodeType.CONDITIONAL_BEGINNING);
    Node v3 = new Node("v3", 3, NodeType.REGULAR);
    Node v4 = new Node("v4", 4, NodeType.REGULAR);
    Node v5 = new Node("v5", 2, NodeType.REGULAR);
    Node v6 = new Node("v6", 0, NodeType.CONDITIONAL_END);
    Node v7 = new Node("v7", 1, NodeType.REGULAR);
    Node v8 = new Node("v8", 1, NodeType.REGULAR);
    Node v9 = new Node("v9", 1, NodeType.REGULAR);

    Graph graph = new Graph();

    graph.addAllNode(v1, v2, v3, v4, v5, v6, v7, v8, v9);

    graph.addDirectedArc(v1, v2);
    graph.addDirectedArc(v1, v5);
    graph.addDirectedArc(v2, v3);
    graph.addDirectedArc(v2, v4);
    graph.addDirectedArc(v4, v6);
    graph.addDirectedArc(v3, v6);
    graph.addDirectedArc(v5, v8);
    graph.addDirectedArc(v5, v7);
    graph.addDirectedArc(v6, v7);
    graph.addDirectedArc(v6, v8);
    graph.addDirectedArc(v8, v9);
    graph.addDirectedArc(v7, v9);

    return cpTaskService.computeWorstCaseWorkload(graph);
  }
}
