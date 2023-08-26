package it.unifi.swe4es.sv.cptask.mappers;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.models.NodeV2;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NodeMapper {

  NodeMapper INSTANCE = Mappers.getMapper(NodeMapper.class);

  /*@Deprecated
  NodeDTO toDTO(Node node);*/

  NodeDTO toDTO(NodeV2 node);

  /*@Deprecated
  Node fromDTO(NodeDTO nodeDTO);*/

  //ToDo: rename and remove deprecated methods
  NodeV2 fromDTOV2(NodeDTO nodeDTO);

}
