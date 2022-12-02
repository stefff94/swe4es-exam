package it.unifi.swe4es.sv.cptask.models;

import java.util.Objects;

public class Node {

  private final String label;
  private final Integer weight;
  private final NodeType type;
  private boolean visited;

  public Node(String label, Integer weight, NodeType type) {
    this.label = label;
    this.weight = weight;
    this.type = type;
    this.visited = false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return Objects.equals(label, node.label) && Objects.equals(weight, node.weight) && type == node.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, weight, type);
  }

  @Override
  public String toString() {
    return "Node{" +
            "label='" + label + '\'' +
            ", weight=" + weight +
            ", type=" + type +
            '}';
  }

  public boolean isVisited() {
    return visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  public String getLabel() {
    return label;
  }

  public Integer getWeight() {
    return weight;
  }

  public NodeType getType() {
    return type;
  }

  public boolean isBeginCondition() {
    return this.type.equals(NodeType.CONDITIONAL_BEGINNING);
  }
}
