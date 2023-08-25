package it.unifi.swe4es.sv.cptask.models;

public enum NodeType {
  REGULAR("REGULAR"),
  CONDITIONAL_BEGINNING("CONDITIONAL_BEGINNING"),
  CONDITIONAL_END("CONDITIONAL_END");

  public final String label;

  NodeType(String label) {
    this.label = label;
  }
}
