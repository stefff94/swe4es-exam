package it.unifi.swe4es.sv.cptask.mappers;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.Node;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NodeMapper {

  NodeMapper INSTANCE = Mappers.getMapper(NodeMapper.class);
  NodeDTO toDTO(Node node);

}
