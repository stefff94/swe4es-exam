package it.unifi.swe4es.sv.cptask.dto;

import it.unifi.swe4es.sv.cptask.models.NodeType;

public class NodeDTO {

  private String label;
  private Integer weight;
  private NodeType type;

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
}
