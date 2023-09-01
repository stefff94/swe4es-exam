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
    private Node from;
    @ManyToOne
    @JoinColumn(name = "node_to_id")
    private Node to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }
}
