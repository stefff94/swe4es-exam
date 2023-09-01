package it.unifi.swe4es.sv.cptask.services;

import com.google.common.collect.Lists;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import org.oristool.eulero.modelgeneration.RandomGenerator;
import org.oristool.eulero.modelgeneration.blocksettings.BlockTypeSetting;
import org.oristool.eulero.modelgeneration.blocksettings.DAGBlockSetting;
import org.oristool.eulero.modelgeneration.blocksettings.WellNestedBlockSetting;
import org.oristool.eulero.modelgeneration.blocksettings.XORBlockSetting;
import org.oristool.eulero.modeling.*;
import org.oristool.models.stpn.trees.StochasticTransitionFeature;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EuleroService {

    public GraphDTO generateRandomGraph() {
        Set<BlockTypeSetting> level1Settings = new HashSet<>();

        // Level 1
        BlockTypeSetting DAG = new DAGBlockSetting(1.);
        level1Settings.add(DAG);

        // Level 2
        Set<BlockTypeSetting> level2Settings = new HashSet<>();
        int maximumBreadth = 3;
        XORBlockSetting xor = new XORBlockSetting(0.1, maximumBreadth);
        WellNestedBlockSetting simple = new WellNestedBlockSetting("Simple", 0.9, 1, 1);
        /*ANDBlockSetting and = new ANDBlockSetting(0.45, maximumBreadth);
        SEQBlockSetting seq = new SEQBlockSetting(0.45, maximumBreadth);*/
        level2Settings.add(xor);
        /*level2Settings.add(and);
        level2Settings.add(seq);*/
        level2Settings.add(simple);

        ArrayList<Set<BlockTypeSetting>> settings = new ArrayList<>();
        settings.add(level1Settings);
        settings.add(level2Settings);

        StochasticTransitionFeature feature = StochasticTransitionFeature. newUniformInstance("0", "1");
        RandomGenerator randomGenerator = new RandomGenerator(feature, settings);

        org.oristool.eulero.modeling.DAG model = (DAG) randomGenerator.generateBlock(settings.size());

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setName(String.valueOf(UUID.randomUUID()));
        model.activities().forEach(a -> buildCpTaskGraph(graphDTO, a));

        return graphDTO;
    }

    private void buildCpTaskGraph(GraphDTO graphDTO, Activity activity) {
        buildCpTaskGraph(graphDTO, activity, true);
    }
    private void buildCpTaskGraph(GraphDTO graphDTO, Activity activity, boolean randomWeights) {
        if (activity.type().equals(ActivityType.SIMPLE)) {

            NodeDTO currentNode = generateOrGetNode(graphDTO.getNodes(), activity.name(), NodeType.REGULAR, randomWeights);

            graphDTO.addNode(currentNode);

            activity.pre().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_END", NodeType.CONDITIONAL_END, randomWeights);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR, randomWeights);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(apNode, currentNode);
            });

            activity.post().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_BEGIN", NodeType.CONDITIONAL_BEGINNING, randomWeights);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR, randomWeights);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(currentNode, apNode);
            });

        } else if (activity.type().equals(ActivityType.XOR)) {
            NodeDTO xorBegin = generateOrGetNode(graphDTO.getNodes(), activity.name() + "_BEGIN", NodeType.CONDITIONAL_BEGINNING, randomWeights);
            graphDTO.addNode(xorBegin);

            NodeDTO xorEND = generateOrGetNode(graphDTO.getNodes(), activity.name() + "_END", NodeType.CONDITIONAL_END, randomWeights);
            graphDTO.addNode(xorEND);

            activity.activities().forEach(xa -> {
                NodeDTO xaNode = generateOrGetNode(graphDTO.getNodes(), xa.name(), NodeType.REGULAR, randomWeights);
                graphDTO.addNode(xaNode);
                graphDTO.addDirectedArc(xorBegin, xaNode);
                graphDTO.addDirectedArc(xaNode, xorEND);
            });

            activity.pre().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_END", NodeType.CONDITIONAL_END, randomWeights);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR, randomWeights);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(apNode, xorBegin);
            });

            activity.post().forEach(ap -> {
                NodeDTO apNode;

                if (ap.type().equals(ActivityType.XOR)) {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name() + "_BEGIN", NodeType.CONDITIONAL_BEGINNING, randomWeights);
                } else {
                    apNode = generateOrGetNode(graphDTO.getNodes(), ap.name(), NodeType.REGULAR, randomWeights);
                }

                graphDTO.addNode(apNode);
                graphDTO.addDirectedArc(xorEND, apNode);
            });

        } else {
            activity.activities().forEach(a -> buildCpTaskGraph(graphDTO, a));
        }
    }

    private NodeDTO generateOrGetNode(Set<NodeDTO> nodeDTOList, String label, NodeType type, boolean randomWeight) {

        if (label.contains("DAG") && label.contains("BEGIN")) {
            label = "SOURCE";
        }

        if (label.contains("DAG") && label.contains("END")) {
            label = "SINK";
        }

        String finalLabel = label;

        Integer weight;
        if (randomWeight) {
            weight = ThreadLocalRandom.current().nextInt(0, 11);
        } else {
            weight = retrieveWeight(finalLabel);
        }

        return nodeDTOList.stream()
                .filter(n -> n.getLabel().equals(finalLabel))
                .findFirst()
                .orElse(new NodeDTO(label, weight, type));
    }

    private Integer retrieveWeight(String label) {
        int weight;

        switch (label) {
            case "Dag_BEGIN":
            case "v8":
            case "Dag_END":
            case "v7":
            case "Xor(v4, v3)_BEGIN":
                weight = 1;
                break;
            case "v3":
                weight = 3;
                break;
            case "v4":
                weight = 4;
                break;
            case "v5":
                weight = 2;
                break;
            case "Xor(v4, v3)_END":
                weight = 0;
                break;
            default:
                weight = 0;
                break;
        }

        return weight;
    }

    public GraphDTO generateDemoGraph() {
        StochasticTransitionFeature f = StochasticTransitionFeature.newDeterministicInstance("2");
        XOR xor = new XOR(
                "Xor(v4, v3)",
                List.of(
                        new Simple("v4", f),
                        new Simple("v3", f)
                ),
                List.of(0.5, 0.5)
        );

        Simple v5 = new Simple("v5", f);
        Simple v7 = new Simple("v7", f);
        Simple v8 = new Simple("v8", f);

        DAG dag = DAG.empty("Dag");
        dag.end().addPrecondition(v7, v8);
        v8.addPrecondition(v5, xor);
        v7.addPrecondition(v5, xor);
        v5.addPrecondition(dag.begin());
        xor.addPrecondition(dag.begin());

        dag.setMin(dag.getMinBound(dag.end()));
        dag.setMax(dag.getMaxBound(dag.end()));
        dag.setActivities(Lists.newArrayList(v5, v7, v8, xor));

        /*DAG seq = DAG.sequence("SEQ",
                new Simple("v1", f),
                dag,
                new Simple("v9", f)
        );

        return seq;*/

        GraphDTO graphDTO = new GraphDTO();
        graphDTO.setName(String.valueOf(UUID.randomUUID()));
        dag.activities().forEach(a -> buildCpTaskGraph(graphDTO, a, false));

        return graphDTO;
    }
}
