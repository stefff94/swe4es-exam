package it.unifi.swe4es.sv.cptask.models;

// import jakarta.persistence.*;
import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "graphs_v2")
public class GraphV2 {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy="graph")
    private Set<NodeV2> nodes;

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

    public Set<NodeV2> getNodes() {
        return nodes;
    }

    public void setNodes(Set<NodeV2> nodes) {
        this.nodes = nodes;
    }
}
