package it.unifi.swe4es.sv.cptask.models;

import jakarta.persistence.*;

@Entity
@Table(name = "graph_path_elements")
public class GraphPathElement {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "graph_path_id", nullable = false)
    private GraphPath graphPath;
    @ManyToOne
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;
    private Integer ord;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GraphPath getGraphPath() {
        return graphPath;
    }

    public void setGraphPath(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer order) {
        this.ord = order;
    }
}
