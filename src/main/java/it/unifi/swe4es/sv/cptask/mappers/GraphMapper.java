package it.unifi.swe4es.sv.cptask.mappers;

import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper
public interface GraphMapper {

    GraphMapper INSTANCE = Mappers.getMapper(GraphMapper.class);

    default GraphDTO toDTO(GraphV2 graph) {
        GraphDTO graphDTO = new GraphDTO();
        graphDTO.addAllNodes(graph.getNodes().stream().map(NodeMapper.INSTANCE::toDTO));

        graph.getNodes().forEach(n -> n.getSuccesors().stream()
                .map(NodeMapper.INSTANCE::toDTO)
                .forEach(ns -> graphDTO.addDirectedArc(NodeMapper.INSTANCE.toDTO(n), ns)));

        return graphDTO;
    }
}
