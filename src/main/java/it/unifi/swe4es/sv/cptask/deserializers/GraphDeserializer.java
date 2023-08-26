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

    /*{
        "name": "demo_graph_1",
        "adjList": [
            {
                "node": {
                    "label": "v1",
                    "weight": 1,
                    "type": "REGULAR"
                },
                "list": [
                    {
                        "id": null,
                        "label": "v2",
                        "weight": 1,
                        "type": "CONDITIONAL_BEGINNING"
                    },
                    {
                        "id": null,
                        "label": "v5",
                        "weight": 2,
                        "type": "REGULAR"
                    }
                ]
            }
        ]
    }*/

    @Override
    public GraphDTO deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        GraphDTO graphDTO = new GraphDTO();

        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        graphDTO.setName(jsonNode.path("name").textValue());

        JsonNode adjList = jsonNode.path("adjList");

        // Map<NodeDTO, List<NodeDTO>> adjListMap = new HashMap<>();

        if (adjList.isArray()) {
            for (JsonNode adjListElement: adjList) {
                JsonNode node = adjListElement.path("node");

                NodeDTO nodeDTO = new NodeDTO();
                nodeDTO.setLabel(node.path("label").textValue());
                nodeDTO.setWeight(node.path("weight").asInt());
                nodeDTO.setType(NodeType.valueOf(node.path("type").textValue()));

                graphDTO.addNode(nodeDTO);

                JsonNode list = adjListElement.path("list");

                // List<NodeDTO> nodeDTOList = new ArrayList<>();

                if (list.isArray()) {
                    for (JsonNode listElement: list) {
                        NodeDTO nodeDTOLE = new NodeDTO();
                        nodeDTOLE.setLabel(listElement.path("label").textValue());
                        nodeDTOLE.setWeight(listElement.path("weight").intValue());
                        nodeDTOLE.setType(NodeType.valueOf(listElement.path("type").textValue()));

                        graphDTO.addNode(nodeDTOLE);

                        graphDTO.addDirectedArc(nodeDTO, nodeDTOLE);

                        // nodeDTOList.add(nodeDTOLE);
                    }
                }

                // adjListMap.put(nodeDTO, nodeDTOList);
            }
        }

//        int id = (Integer) ((IntNode) node.get("id")).numberValue();
//        String itemName = node.get("itemName").asText();
//        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
//
//        return new Item(id, itemName, new User(userId, null));
        return graphDTO;
    }
}
