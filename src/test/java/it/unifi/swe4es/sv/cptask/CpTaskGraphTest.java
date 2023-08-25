package it.unifi.swe4es.sv.cptask;

import org.junit.Test;
import org.oristool.eulero.modelgeneration.RandomGenerator;
import org.oristool.eulero.modelgeneration.blocksettings.*;
import org.oristool.eulero.modeling.Activity;
import org.oristool.eulero.modeling.DAG;
import org.oristool.eulero.modeling.Simple;
import org.oristool.eulero.modeling.XOR;
import org.oristool.models.stpn.trees.StochasticTransitionFeature;
import org.oristool.petrinet.PetriNet;
import org.oristool.petrinet.Place;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CpTaskGraphTest {

    @Test
    public void test() {
        StochasticTransitionFeature feature = StochasticTransitionFeature.newUniformInstance("0", "1");

        /*DAG.sequence("Q",
                DAG.forkJoin("Q1",
                        new Simple("Q1A", feature),
                        new Simple("Q1B", feature)
                ),
                DAG.forkJoin("Q2",
                        new Simple("Q2A", feature),
                        new Simple("Q2B", feature)
                )
        );*/

        Simple v1 = new Simple("v1", feature);
        Simple v2 = new Simple("v2", feature);
        Simple v3 = new Simple("v3", feature);
        Simple v4 = new Simple("v4", feature);
        Simple v5 = new Simple("v5", feature);
        Simple v6 = new Simple("v6", feature);
        Simple v7 = new Simple("v7", feature);
        Simple v8 = new Simple("v8", feature);
        Simple v9 = new Simple("v9", feature);

        XOR cond = new XOR("COND", List.of(v3, v4), List.of(0.3, 0.7));

        DAG cpTask = DAG.empty("cp-task");

        v1.addPrecondition(cpTask.begin());
        v2.addPrecondition(v1);
        v5.addPrecondition(v1);

        cond.addPrecondition(v2);

        v6.addPrecondition(cond);

        v7.addPrecondition(v5, v6);
        v8.addPrecondition(v5, v6);

        v9.addPrecondition(v7, v8);

        cpTask.end().addPrecondition(v9);
        cpTask.setMin(cpTask.getMinBound(cpTask.end()));
        cpTask.setMax(cpTask.getMaxBound(cpTask.end()));
        cpTask.setActivities(List.of(v1, v2, cond, v5, v6, v7, v8, v9));




    }

    @Test
    public void generateGraphTest() {
        int concurrencyDegree , sequenceFactor;
        concurrencyDegree = sequenceFactor = 3;

        Set<BlockTypeSetting> level1Settings = new HashSet<>();
        BlockTypeSetting AND = new ANDBlockSetting(0.5, concurrencyDegree);
        BlockTypeSetting SEQ = new SEQBlockSetting(0.5, sequenceFactor);
        level1Settings.add(AND);
        level1Settings.add(SEQ);

        Set<BlockTypeSetting> level2Settings = new HashSet<>();
        BlockTypeSetting DAG = new DAGBlockSetting(1.);
        level2Settings.add(DAG);

        ArrayList<Set<BlockTypeSetting>> settings = new ArrayList<>();
        settings.add(level1Settings);
        settings.add(level2Settings);

        StochasticTransitionFeature feature = StochasticTransitionFeature. newUniformInstance("0", "1");
        RandomGenerator randomGenerator = new RandomGenerator(feature, settings);

        Activity model = randomGenerator.generateBlock(settings.size());


        System.out.println(model.toString());
    }

    @Test
    public void generateCpTaskGraph() {
        int concurrencyDegree , sequenceFactor, orFactor;
        concurrencyDegree = sequenceFactor = orFactor = 3;

        Set<BlockTypeSetting> level1Settings = new HashSet<>();
        BlockTypeSetting AND = new ANDBlockSetting(0.5, concurrencyDegree);
        BlockTypeSetting SEQ = new SEQBlockSetting(0.5, sequenceFactor);
        level1Settings.add(AND);
        level1Settings.add(SEQ);

        /*public DAGBlockSetting(double probability, int minimumLevels, int maximumLevels, int minimumLevelBreadth, int maximumLevelBreadth, int maximumAdjacencyDistance, int minimumNodeConnection, int maximumNodeConnection) {
            super("DAG", probability);
            this.minimumLevels = minimumLevels;
            this.maximumLevels = maximumLevels;
            this.minimumLevelBreadth = minimumLevelBreadth;
            this.maximumLevelBreadth = maximumLevelBreadth;
            this.maximumAdjacencyDistance = maximumAdjacencyDistance;
            this.maximumNodeConnection = maximumNodeConnection;
            this.minimumNodeConnection = minimumNodeConnection;
        }

    public DAGBlockSetting(double probability) {
            super("DAG", probability);
            this.minimumLevels = 2;
            this.maximumLevels = 3;
            this.minimumLevelBreadth = 2;
            this.maximumLevelBreadth = 3;
            this.maximumAdjacencyDistance = 1;
            this.maximumNodeConnection = 2;
            this.minimumNodeConnection = 1;
        }*/

        Set<BlockTypeSetting> level2Settings = new HashSet<>();
        // BlockTypeSetting DAG = new DAGBlockSetting(1.);
        /*BlockTypeSetting DAG = new DAGBlockSetting(1.,
                10, 10,
                2, 7,
                3, 3, 5);*/

        BlockTypeSetting DAG = new DAGBlockSetting(1.,
                2, 3,
                2, 3,
                1, 1, 2);
        level2Settings.add(DAG);
        /*BlockTypeSetting AND2Liv = new ANDBlockSetting(0.3, concurrencyDegree);
        BlockTypeSetting SEQ2Liv = new SEQBlockSetting(0.3, sequenceFactor);
        BlockTypeSetting XOR2Liv = new XORBlockSetting(0.4, sequenceFactor);
        level2Settings.add(AND2Liv);
        level2Settings.add(SEQ2Liv);
        level2Settings.add(XOR2Liv);*/

        Set<BlockTypeSetting> level3Settings = new HashSet<>();
        XORBlockSetting XOR3Liv = new XORBlockSetting(0.1, sequenceFactor);
        ANDBlockSetting andBlockSetting = new ANDBlockSetting(0.45, sequenceFactor);
        SEQBlockSetting seqBlockSetting = new SEQBlockSetting(0.45, sequenceFactor);

        // SEQBlockSetting SEQ3Liv = new SEQBlockSetting(1, sequenceFactor);
        WellNestedBlockSetting simple = new WellNestedBlockSetting("Simple", 0.9, 1, 1);
        // level3Settings.add(simple);

        level3Settings.add(andBlockSetting);
        level3Settings.add(seqBlockSetting);
        level3Settings.add(XOR3Liv);


        ArrayList<Set<BlockTypeSetting>> settings = new ArrayList<>();
        // settings.add(level1Settings);
        settings.add(level2Settings);
        settings.add(level3Settings);
        settings.add(level3Settings);

        StochasticTransitionFeature feature = StochasticTransitionFeature. newUniformInstance("0", "1");
        RandomGenerator randomGenerator = new RandomGenerator(feature, settings);

        DAG model = (DAG) randomGenerator.generateBlock(settings.size());

        PetriNet pn = new PetriNet();
        Place in = pn.addPlace("pBEGIN");
        Place out = pn.addPlace("pEND");
        model.buildSTPN(pn, in, out, 1);

        System.out.println(model.toString());

        model.edges().forEach(e -> System.out.println("PRE: " + e.getPre() + " - POST: " + e.getPost()));

        System.out.println(model.yaml());
    }
}
