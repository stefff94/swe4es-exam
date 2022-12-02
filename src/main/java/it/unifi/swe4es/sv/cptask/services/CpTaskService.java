package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CpTaskService {

  private final GraphService graphService;

  @Autowired
  public CpTaskService(GraphService graphService) {
    this.graphService = graphService;
  }

  public Integer computeWCET(Set<Node> nodes) {
    return nodes.stream()
            .map(Node::getWeight)
            .mapToInt(Integer::intValue)
            .sum();
  }

  public Integer computeWorstCaseWorkload(Graph graph) {

    final List<Node> reversedTopologicalSort = new ArrayList<>(graphService.topologicalSort(graph));
    Collections.reverse(reversedTopologicalSort);

    Map<Node, Set<Node>> subgraphs = new HashMap<>();

    Node sink = reversedTopologicalSort.get(0);
    Node source = reversedTopologicalSort.get(reversedTopologicalSort.size() - 1);
    subgraphs.put(sink, Stream.of(sink).collect(Collectors.toCollection(HashSet::new)));

    for (Node currentNode : reversedTopologicalSort) {
      final List<Node> successors = graph.getAdjNodes(currentNode);
      if (successors.size() > 0) {
        if (currentNode.isBeginCondition()) {
          // per ogni successor v devo calcolare il C(S(v))
          // prendere il v che totalizza il C(S(v)) massimo
          // final OptionalInt max = successors.stream().map(s -> computeWCET(subgraphs.get(s))).mapToInt(Integer::intValue).max();

          final Node vStar = successors.stream()
                  .max(Comparator.comparing(s -> computeWCET(subgraphs.get(s))))
                  .orElse(null);

          Set<Node> tmpSet = new HashSet<>();
          tmpSet.add(currentNode);
          tmpSet.addAll(subgraphs.get(vStar));

          subgraphs.put(currentNode, tmpSet);

        } else {
          Set<Node> tmpSet = new HashSet<>();
          successors.stream()
                  .map(subgraphs::get)
                  .forEach(tmpSet::addAll);

          tmpSet.add(currentNode);

          subgraphs.put(currentNode, tmpSet);
        }
      }
    }
    // return C(S(vSource))
    return computeWCET(subgraphs.get(source));
  }
}
