package it.unifi.swe4es.sv.cptask.repositories;

import it.unifi.swe4es.sv.cptask.models.GraphV2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphV2Repository extends CrudRepository<GraphV2, Long> {
}
