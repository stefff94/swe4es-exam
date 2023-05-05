package it.unifi.swe4es.sv.cptask.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unifi.swe4es.sv.cptask.models.NodeType;

import java.util.Objects;

public class NodeDTO {

  private Long id;
  private String label;
  private Integer weight;
  private NodeType type;
  @JsonIgnore
  private boolean visited;

  public NodeDTO() {
  }

  public NodeDTO(String label, Integer weight, NodeType type) {
    this.label = label;
    this.weight = weight;
    this.type = type;
    this.visited = false;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public NodeType getType() {
    return type;
  }

  public void setType(NodeType type) {
    this.type = type;
  }

  public boolean isVisited() {
    return visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  @JsonIgnore
  public boolean isBeginCondition() {
    return this.type.equals(NodeType.CONDITIONAL_BEGINNING);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NodeDTO nodeDTO = (NodeDTO) o;
    return Objects.equals(label, nodeDTO.label) && Objects.equals(weight, nodeDTO.weight) && type == nodeDTO.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, weight, type);
  }

  @Override
  public String toString() {
    return "NodeDTO{" +
            "label='" + label + '\'' +
            ", weight=" + weight +
            ", type=" + type +
            '}';
  }
}
