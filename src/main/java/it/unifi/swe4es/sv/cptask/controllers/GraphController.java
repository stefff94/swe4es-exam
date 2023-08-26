package it.unifi.swe4es.sv.cptask.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.unifi.swe4es.sv.cptask.deserializers.GraphDeserializer;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.mappers.GraphMapper;
import it.unifi.swe4es.sv.cptask.services.DemoGraphService;
import it.unifi.swe4es.sv.cptask.services.EuleroService;
import it.unifi.swe4es.sv.cptask.services.GraphV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {
    private final GraphV2Service graphService;
    private final DemoGraphService demoGraphService;
    private final EuleroService euleroService;
    @Autowired
    public GraphController(GraphV2Service graphService,
                           DemoGraphService demoGraphService,
                           EuleroService euleroService) {
        this.graphService = graphService;
        this.demoGraphService = demoGraphService;
        this.euleroService = euleroService;
    }

    @GetMapping("{id}")
    public ResponseEntity<GraphDTO> getGraph(@PathVariable Long id) {
        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(graphService.getGraph(id));

        return ResponseEntity.ok().body(graphDTO);
    }

    @PostMapping("/new")
    public ResponseEntity<Long> newGraph(@RequestBody(required = false) GraphDTO graphDTO) {
        Long graphId;

        if (graphDTO != null) {
            graphId = graphService.insertNewGraph(graphDTO);
        } else {
            graphId = graphService.insertNewGraph(demoGraphService.getDemoGraph());
        }

        return ResponseEntity.ok().body(graphId);
    }

    @PostMapping("/new/json")
    public ResponseEntity<Long> newGraph(@RequestBody String jsonGraph) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("GraphDeserializer",
                new Version(1, 0, 0, null, null, null));

        module.addDeserializer(GraphDTO.class, new GraphDeserializer());
        mapper.registerModule(module);
        GraphDTO graphDTO = mapper.readValue(jsonGraph, GraphDTO.class);

        Long graphId = graphService.insertNewGraph(graphDTO);

        return ResponseEntity.ok()
                .body(graphId);

    }

    @GetMapping("/random")
    public ResponseEntity<Long> generateRandomGraph() {
        Long graphId = graphService
                .insertNewGraph(euleroService.generateRandomGraph());

        return ResponseEntity.ok(graphId);
    }

    /*@GetMapping("/random")
    public ResponseEntity<GraphDTO> generateRandomGraph() {

        Set<BlockTypeSetting> level1Settings = new HashSet<>();

        // Level 1
        BlockTypeSetting DAG = new DAGBlockSetting(1.);
        level1Settings.add(DAG);

        // Level 2
        Set<BlockTypeSetting> level2Settings = new HashSet<>();
        int maximumBreadth = 3;
        XORBlockSetting xor = new XORBlockSetting(0.1, maximumBreadth);
        WellNestedBlockSetting simple = new WellNestedBlockSetting("Simple", 0.9, 1, 1);
        *//*ANDBlockSetting and = new ANDBlockSetting(0.45, maximumBreadth);
        SEQBlockSetting seq = new SEQBlockSetting(0.45, maximumBreadth);*//*
        level2Settings.add(xor);
        *//*level2Settings.add(and);
        level2Settings.add(seq);*//*
        level2Settings.add(simple);

        ArrayList<Set<BlockTypeSetting>> settings = new ArrayList<>();
        settings.add(level1Settings);
        settings.add(level2Settings);

        StochasticTransitionFeature feature = StochasticTransitionFeature. newUniformInstance("0", "1");
        RandomGenerator randomGenerator = new RandomGenerator(feature, settings);

        DAG model = (DAG) randomGenerator.generateBlock(settings.size());

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setName(String.valueOf(UUID.randomUUID()));
        model.activities().forEach(a -> buildCpTaskGraph(graphDTO, a));

        // return ResponseEntity.ok(model.toString());
        return ResponseEntity.ok(graphDTO);
    }

    public void buildCpTaskGraph(GraphDTO graphDTO, Activity activity) {
        if (activity.type().equals(ActivityType.SIMPLE)) {

            NodeDTO currentNode = generateOrGetNode(graphDTO.getNodes(), activity.name(), NodeType.REGULAR);

            graphDTO.addNode(currentNode);

            activity.pre().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_END", NodeType.CONDITIONAL_END);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(apNode, currentNode);
            });

            activity.post().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_BEGIN", NodeType.CONDITIONAL_BEGINNING);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(currentNode, apNode);
            });

        } else if (activity.type().equals(ActivityType.XOR)) {
            NodeDTO xorBegin = generateOrGetNode(graphDTO.getNodes(), activity.name() + "_BEGIN", NodeType.CONDITIONAL_BEGINNING);
            graphDTO.addNode(xorBegin);

            NodeDTO xorEND = generateOrGetNode(graphDTO.getNodes(), activity.name() + "_END", NodeType.CONDITIONAL_END);
            graphDTO.addNode(xorEND);

            activity.activities().forEach(xa -> {
                NodeDTO xaNode = generateOrGetNode(graphDTO.getNodes(), xa.name(), NodeType.REGULAR);
                graphDTO.addNode(xaNode);
                graphDTO.addDirectedArc(xorBegin, xaNode);
                graphDTO.addDirectedArc(xaNode, xorEND);
            });

            activity.pre().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_END", NodeType.CONDITIONAL_END);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(apNode, xorBegin);
            });

            activity.post().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_BEGIN", NodeType.CONDITIONAL_BEGINNING);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(xorEND, apNode);
            });

        } else {
            activity.activities().forEach(a -> buildCpTaskGraph(graphDTO, a));
        }
    }

    public NodeDTO generateOrGetNode(Set<NodeDTO> nodeDTOList, String label, NodeType type) {

        if (label.contains("DAG") && label.contains("BEGIN")) {
            label = "SOURCE";
        }

        if (label.contains("DAG") && label.contains("END")) {
            label = "SINK";
        }

        String finalLabel = label;
        return nodeDTOList.stream()
                .filter(n -> n.getLabel().equals(finalLabel))
                .findFirst()
                .orElse(new NodeDTO(label, ThreadLocalRandom.current().nextInt(0, 11), type));
    }*/
}
