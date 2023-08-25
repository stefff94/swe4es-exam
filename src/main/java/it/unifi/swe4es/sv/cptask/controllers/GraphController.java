package it.unifi.swe4es.sv.cptask.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.unifi.swe4es.sv.cptask.deserializers.GraphDeserializer;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.mappers.GraphMapper;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import it.unifi.swe4es.sv.cptask.services.DemoGraphService;
import it.unifi.swe4es.sv.cptask.services.EuleroService;
import it.unifi.swe4es.sv.cptask.services.GraphService;
import it.unifi.swe4es.sv.cptask.services.GraphV2Service;
import org.oristool.eulero.modelgeneration.RandomGenerator;
import org.oristool.eulero.modelgeneration.blocksettings.*;
import org.oristool.eulero.modeling.Activity;
import org.oristool.eulero.modeling.ActivityType;
import org.oristool.eulero.modeling.DAG;
import org.oristool.models.stpn.trees.StochasticTransitionFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/v1/graph")
public class GraphController {

    private final GraphService graphService;
    private final GraphV2Service graphV2Service;
    private final DemoGraphService demoGraphService;
    private final EuleroService euleroService;
    @Autowired
    public GraphController(GraphService graphService,
                           GraphV2Service graphV2Service,
                           DemoGraphService demoGraphService,
                           EuleroService euleroService) {

        this.graphService = graphService;
        this.graphV2Service = graphV2Service;
        this.demoGraphService = demoGraphService;
        this.euleroService = euleroService;
    }

    /*@PostMapping("/new-node")
    public NodeDTO newNode(@RequestBody Node node) {
        return NodeMapper.INSTANCE.toDTO(graphService.insertNewNode(node));
    }*/

    @GetMapping("{id}")
    public ResponseEntity<GraphDTO> getGraph(@PathVariable Long id) {
        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(graphService.getGraph(id));

        return ResponseEntity.ok().body(graphDTO);
    }

    /*@PostMapping("/new")
    public ResponseEntity<GraphDTO> newGraph(@RequestBody GraphDTO graph) {
        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(
                graphService.insertNewGraph(GraphMapper.INSTANCE.fromDTO(graph)));

        return ResponseEntity.ok().body(graphDTO);

    }*/

    @PostMapping("/new")
    public ResponseEntity<GraphDTO> newGraph(@RequestBody String graphJson) throws JsonProcessingException {
        /*GraphDTO graph = new ObjectMapper().readValue(graphJson, GraphDTO.class);

        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(
                graphService.insertNewGraph(GraphMapper.INSTANCE.fromDTO(graph)));*/

        GraphDTO graphDTO = GraphMapper.INSTANCE.toDTO(
                graphService.insertNewGraph(GraphMapper.INSTANCE.fromDTO(demoGraphService.getDemoGraph())));

        return ResponseEntity.ok().body(graphDTO);

    }

    @PostMapping("/newV2")
    public ResponseEntity<GraphDTO> newGraphV2(@RequestBody String jsonGraph) throws JsonProcessingException {

        // GraphDTO graphDTO = new ObjectMapper().readValue(jsonGraph, GraphDTO.class);


        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module =
                new SimpleModule("GraphDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(GraphDTO.class, new GraphDeserializer());
        mapper.registerModule(module);
        GraphDTO graphDTO = mapper.readValue(jsonGraph, GraphDTO.class);

        // graphV2Service.insertNewGraph(demoGraphService.getDemoGraph());

        return ResponseEntity.ok().body(graphDTO);

    }

    public ResponseEntity<String> generateRandomGraph() {
        //Todo: implement
        graphV2Service.insertNewGraph(euleroService.generateRandomGraph());

        return ResponseEntity.ok("todo");
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
