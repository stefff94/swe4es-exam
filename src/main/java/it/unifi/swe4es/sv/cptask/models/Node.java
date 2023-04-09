package it.unifi.swe4es.sv.cptask.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "nodes")
public class Node {

    @Id @GeneratedValue
    private Long id;
    private String label;
    private Integer weight;
    @Enumerated(EnumType.STRING)
    private NodeType type;
    @OneToMany(mappedBy = "node")
    private Set<GraphPath> graphPaths;
    @OneToMany(mappedBy = "node")
    private Set<GraphPathElement> graphPathElements;

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

    public Set<GraphPath> getGraphPaths() {
        return graphPaths;
    }

    public void setGraphPaths(Set<GraphPath> graphPaths) {
        this.graphPaths = graphPaths;
    }

    public Set<GraphPathElement> getGraphPathElements() {
        return graphPathElements;
    }

    public void setGraphPathElements(Set<GraphPathElement> graphPathElements) {
        this.graphPathElements = graphPathElements;
    }
}
