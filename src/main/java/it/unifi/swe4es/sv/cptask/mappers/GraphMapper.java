package it.unifi.swe4es.sv.cptask.mappers;

import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.IntStream;

@Mapper
public interface GraphMapper {

    GraphMapper INSTANCE = Mappers.getMapper(GraphMapper.class);

    default GraphDTO toDTO(Graph graph) {
        Map<NodeDTO, List<NodeDTO>> adjList = new HashMap<>();

        Set<GraphPath> graphPaths = graph.getGraphPath();

        for (GraphPath gp : graphPaths) {

            NodeDTO n = NodeMapper.INSTANCE.toDTO(gp.getNode());

            List<NodeDTO> nList = gp.getElements().stream()
                    .sorted(Comparator.comparingInt(GraphPathElement::getOrd))
                    .map(GraphPathElement::getNode)
                    .map(NodeMapper.INSTANCE::toDTO)
                    .toList();

            adjList.put(n, nList);
        }

        return new GraphDTO(adjList);
    }

    default Graph fromDTO(GraphDTO graphDTO) {
        Graph graph = new Graph();

        Set<NodeDTO> nodes = graphDTO.getNodes();

        Set<GraphPath> graphPaths = new HashSet<>();

        for (NodeDTO node : nodes) {
            GraphPath graphPath = new GraphPath();
            graphPath.setNode(NodeMapper.INSTANCE.fromDTO(node));

            Set<GraphPathElement> graphPathElements = new HashSet<>();
            List<NodeDTO> adjNodes = graphDTO.getAdjNodes(node);
            IntStream.range(0, adjNodes.size())
                    .forEach(i -> {
                        GraphPathElement graphPathElement = new GraphPathElement();
                        graphPathElement.setNode(NodeMapper.INSTANCE.fromDTO(adjNodes.get(i)));
                        graphPathElement.setOrd(i);

                        graphPathElements.add(graphPathElement);
                    });

            graphPath.setElements(graphPathElements);
            graphPaths.add(graphPath);
        }

        graph.setGraphPath(graphPaths);

        return graph;
    }
}
