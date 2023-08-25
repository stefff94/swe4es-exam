package it.unifi.swe4es.sv.cptask.repositories;

import it.unifi.swe4es.sv.cptask.models.AdjList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjListRepository extends CrudRepository<AdjList, Long> {
}
