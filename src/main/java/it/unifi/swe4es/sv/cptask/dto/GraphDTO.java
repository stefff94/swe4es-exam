package it.unifi.swe4es.sv.cptask.dto;


import java.util.*;
import java.util.stream.Stream;

public class GraphDTO {

  private final Map<NodeDTO, List<NodeDTO>> adjList;

  public GraphDTO() {
    this.adjList = new HashMap<>();
  }

  public void addNode(NodeDTO n) {
    this.adjList.putIfAbsent(n, new LinkedList<>());
  }

  public void addAllNodes(NodeDTO... nodes) {
    addAllNodes(Arrays.stream(nodes));
  }

  public void addAllNodes(Stream<NodeDTO> nodes) {
    nodes.forEach(n -> this.adjList.putIfAbsent(n, new LinkedList<>()));
  }

  public void removeNode(NodeDTO n) {
    this.adjList.values().forEach(l -> l.remove(n));
    this.adjList.remove(n);
  }

  public void addDirectedArc(NodeDTO from, NodeDTO to) {
    if (this.adjList.containsKey(from) && this.adjList.get(from).stream().noneMatch(n -> n.equals(to))) {
      this.adjList.get(from).add(to);
    }
  }

  public void removeDirectedArc(NodeDTO from, NodeDTO to) {
    if (this.adjList.containsKey(from) && this.adjList.get(from).stream().anyMatch(n -> n.equals(to))) {
      this.adjList.get(from).remove(to);
    }
  }

  public Set<NodeDTO> getNodes() {
    return this.adjList.keySet();
  }

  public List<NodeDTO> getAdjNodes(NodeDTO n) {
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
