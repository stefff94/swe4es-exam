package it.unifi.swe4es.sv.cptask.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.NodeType;

import java.io.IOException;

public class GraphDeserializer extends StdDeserializer<GraphDTO> {

    public GraphDeserializer() {
        this(null);
    }

    public GraphDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public GraphDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        GraphDTO graphDTO = new GraphDTO();

        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        graphDTO.setName(jsonNode.path("name").textValue());

        JsonNode adjList = jsonNode.path("adjList");

        if (adjList.isArray()) {
            for (JsonNode adjListElement: adjList) {
                JsonNode node = adjListElement.path("node");

                NodeDTO nodeDTO = new NodeDTO();
                nodeDTO.setLabel(node.path("label").textValue());
                nodeDTO.setWeight(node.path("weight").asInt());
                nodeDTO.setType(NodeType.valueOf(node.path("type").textValue()));

                graphDTO.addNode(nodeDTO);

                JsonNode list = adjListElement.path("list");

                if (list.isArray()) {
                    for (JsonNode listElement: list) {
                        NodeDTO nodeDTOLE = new NodeDTO();
                        nodeDTOLE.setLabel(listElement.path("label").textValue());
                        nodeDTOLE.setWeight(listElement.path("weight").intValue());
                        nodeDTOLE.setType(NodeType.valueOf(listElement.path("type").textValue()));

                        graphDTO.addNode(nodeDTOLE);

                        graphDTO.addDirectedArc(nodeDTO, nodeDTOLE);
                    }
                }
            }
        }

        return graphDTO;
    }
}
