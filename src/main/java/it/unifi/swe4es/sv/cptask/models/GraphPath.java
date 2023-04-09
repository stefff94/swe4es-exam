package it.unifi.swe4es.sv.cptask.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "graph_paths")
public class GraphPath {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "graph_id", nullable = false)
    private Graph graph;
    @ManyToOne
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;
    @OneToMany(mappedBy = "graphPath")
    private Set<GraphPathElement> elements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Set<GraphPathElement> getElements() {
        return elements;
    }

    public void setElements(Set<GraphPathElement> elements) {
        this.elements = elements;
    }
}
