package it.unifi.swe4es.sv.cptask.models;

// import jakarta.persistence.*;
import javax.persistence.*;

@Entity
@Table(name = "nodes_v2")
public class NodeV2 {

    @Id
    @GeneratedValue
    private Long id;
    private String label;
    private Integer weight;
    @Enumerated(EnumType.STRING)
    private NodeType type;
    @ManyToOne
    @JoinColumn(name = "graph_id")
    private GraphV2 graph;

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

    public GraphV2 getGraph() {
        return graph;
    }

    public void setGraph(GraphV2 graph) {
        this.graph = graph;
    }
}
