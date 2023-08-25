package it.unifi.swe4es.sv.cptask.repositories;

import it.unifi.swe4es.sv.cptask.models.Graph;
import org.springframework.data.repository.CrudRepository;

@Deprecated
public interface GraphRepository extends CrudRepository<Graph, Long> {
}
