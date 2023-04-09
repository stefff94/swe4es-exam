package it.unifi.swe4es.sv.cptask.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "graphs")
public class Graph {

    @Id @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy="graph")
    private Set<GraphPath> graphPath;

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

    public Set<GraphPath> getGraphPath() {
        return graphPath;
    }

    public void setGraphPath(Set<GraphPath> graphPath) {
        this.graphPath = graphPath;
    }
}
