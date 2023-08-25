package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.models.NodeType;
import org.oristool.eulero.modelgeneration.RandomGenerator;
import org.oristool.eulero.modelgeneration.blocksettings.BlockTypeSetting;
import org.oristool.eulero.modelgeneration.blocksettings.DAGBlockSetting;
import org.oristool.eulero.modelgeneration.blocksettings.WellNestedBlockSetting;
import org.oristool.eulero.modelgeneration.blocksettings.XORBlockSetting;
import org.oristool.eulero.modeling.Activity;
import org.oristool.eulero.modeling.ActivityType;
import org.oristool.eulero.modeling.DAG;
import org.oristool.models.stpn.trees.StochasticTransitionFeature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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

    private NodeDTO generateOrGetNode(Set<NodeDTO> nodeDTOList, String label, NodeType type) {

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
    }
}
