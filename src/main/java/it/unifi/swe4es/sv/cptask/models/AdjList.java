package it.unifi.swe4es.sv.cptask.models;

// import jakarta.persistence.*;
import javax.persistence.*;

@Entity
@Table(name = "adj_lists")
public class AdjList {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "node_from_id")
    private NodeV2 from;
    @ManyToOne
    @JoinColumn(name = "node_to_id")
    private NodeV2 to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NodeV2 getFrom() {
        return from;
    }

    public void setFrom(NodeV2 from) {
        this.from = from;
    }

    public NodeV2 getTo() {
        return to;
    }

    public void setTo(NodeV2 to) {
        this.to = to;
    }
}
