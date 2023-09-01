package it.unifi.swe4es.sv.cptask.models;

// import jakarta.persistence.*;
import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "graphs")
public class Graph {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy="graph")
    private Set<Node> nodes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }
}
