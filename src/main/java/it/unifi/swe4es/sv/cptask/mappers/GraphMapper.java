package it.unifi.swe4es.sv.cptask.mappers;

import it.unifi.swe4es.sv.cptask.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface GraphMapper {

    GraphMapper INSTANCE = Mappers.getMapper(GraphMapper.class);

    /*default GraphDTO toDTO(GraphDAO graph) {
        Map<NodeDAO, List<NodeDAO>> adjList = new HashMap<>();

        Set<GraphPath> graphPaths = graph.getGraphPath();

        for (GraphPath gp : graphPaths) {

            // adjList.putAll();
            NodeDAO n = gp.getNode(); //Node

            List<NodeDAO> nList = gp.getElements().stream()
                    .sorted(Comparator.comparingInt(GraphPathElement::getOrd))
                    .map(GraphPathElement::getNode)
                    .toList(); //List<Node>

            adjList.put(n, nList);
        }

        return null;
    }*/

    /*GraphDAO fromDTO(GraphDTO graphDTO);*/
}
