package it.unifi.swe4es.sv.cptask.models;

import java.util.*;
import java.util.stream.Stream;

public class Graph {

  private final Map<Node, List<Node>> adjList;

  public Graph() {
    this.adjList = new HashMap<>();
  }

  public void addNode(Node n) {
    this.adjList.putIfAbsent(n, new LinkedList<>());
  }

  public void addAllNodes(Node... nodes) {
    addAllNodes(Arrays.stream(nodes));
  }

  public void addAllNodes(Stream<Node> nodes) {
    nodes.forEach(n -> this.adjList.putIfAbsent(n, new LinkedList<>()));
  }

  public void removeNode(Node n) {
    this.adjList.values().forEach(l -> l.remove(n));
    this.adjList.remove(n);
  }

  public void addDirectedArc(Node from, Node to) {
    if (this.adjList.containsKey(from) && this.adjList.get(from).stream().noneMatch(n -> n.equals(to))) {
      this.adjList.get(from).add(to);
    }
  }

  public void removeDirectedArc(Node from, Node to) {
    if (this.adjList.containsKey(from) && this.adjList.get(from).stream().anyMatch(n -> n.equals(to))) {
      this.adjList.get(from).remove(to);
    }
  }

  public Set<Node> getNodes() {
    return this.adjList.keySet();
  }

  public List<Node> getAdjNodes(Node n) {
    return this.adjList.get(n);
  }

  public String printGraph() {
    StringBuffer sb = new StringBuffer();

    this.adjList.keySet().forEach(n -> {
      sb.append(n);
      sb.append(adjList.get(n));
    });

    return sb.toString();
  }
}
