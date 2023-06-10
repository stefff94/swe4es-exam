package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
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

  public Integer computeWCET(Set<NodeDTO> nodes) {
    return nodes.stream()
            .map(NodeDTO::getWeight)
            .mapToInt(Integer::intValue)
            .sum();
  }

  public Integer computeWorstCaseWorkload(GraphDTO graph) {

    final List<NodeDTO> reversedTopologicalSort = new ArrayList<>(graphService.topologicalSort(graph));
    Collections.reverse(reversedTopologicalSort);

    Map<NodeDTO, Set<NodeDTO>> subgraphs = new HashMap<>();

    NodeDTO sink = reversedTopologicalSort.get(0);
    NodeDTO source = reversedTopologicalSort.get(reversedTopologicalSort.size() - 1);
    subgraphs.put(sink, Stream.of(sink).collect(Collectors.toCollection(HashSet::new)));

    for (NodeDTO currentNode : reversedTopologicalSort) {
      final List<NodeDTO> successors = graph.getAdjNodes(currentNode);
      if (successors.size() > 0) {
        if (currentNode.isBeginCondition()) {
          // per ogni successor v devo calcolare il C(S(v))
          // prendere il v che totalizza il C(S(v)) massimo
          // final OptionalInt max = successors.stream().map(s -> computeWCET(subgraphs.get(s))).mapToInt(Integer::intValue).max();

          final NodeDTO vStar = successors.stream()
                  .max(Comparator.comparing(s -> computeWCET(subgraphs.get(s))))
                  .orElse(null);

          Set<NodeDTO> tmpSet = new HashSet<>();
          tmpSet.add(currentNode);
          tmpSet.addAll(subgraphs.get(vStar));

          subgraphs.put(currentNode, tmpSet);

        } else {
          Set<NodeDTO> tmpSet = new HashSet<>();
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

  public Integer computeZKBuond(GraphDTO graph, Integer m) {
    final List<NodeDTO> reversedTopologicalSort = new ArrayList<>(graphService.topologicalSort(graph));
    Collections.reverse(reversedTopologicalSort);

    Map<NodeDTO, Set<NodeDTO>> s = new HashMap<>();
    Map<NodeDTO, Set<NodeDTO>> t = new HashMap<>();
    Map<NodeDTO, Integer> f = new HashMap<>();

    NodeDTO sink = reversedTopologicalSort.get(0);
    NodeDTO source = reversedTopologicalSort.get(reversedTopologicalSort.size() - 1);

    s.put(sink, Stream.of(sink).collect(Collectors.toCollection(HashSet::new)));
    t.put(sink, Stream.of(sink).collect(Collectors.toCollection(HashSet::new)));
    f.put(sink, sink.getWeight());

    for (NodeDTO currentNode : reversedTopologicalSort) {
      final List<NodeDTO> successors = graph.getAdjNodes(currentNode);
      if (successors.size() > 0) {
        if (currentNode.isBeginCondition()) {
          // todo: implement
          // per ogni successor v devo calcolare il C(S(v))
          // prendere il v che totalizza il C(S(v)) massimo

          // riga 9
          final NodeDTO vStar = successors.stream()
                  .max(Comparator.comparing(s1 -> computeWCET(s.get(s1))))
                  .orElse(null);

          // riga 10
          Set<NodeDTO> tmpSet = new HashSet<>();
          tmpSet.add(currentNode);
          tmpSet.addAll(s.get(vStar));

          s.put(currentNode, tmpSet);

          // riga 11
          final NodeDTO uStar = successors.stream()
                  .max(Comparator.comparing(f::get))
                  .orElse(null);

          // riga 12
          tmpSet = new HashSet<>();
          tmpSet.add(currentNode);
          tmpSet.addAll(t.get(uStar));

          t.put(currentNode, tmpSet);

          // riga 13
          f.put(currentNode, currentNode.getWeight() + f.get(uStar));

        } else {
          // todo: implement

          // riga 15
          Set<NodeDTO> tmpSet = new HashSet<>();
          successors.stream()
                  .map(s::get)
                  .forEach(tmpSet::addAll);

          tmpSet.add(currentNode);

          s.put(currentNode, tmpSet);

          // riga 16-17
          final NodeDTO uStar = successors.stream()
                  .max(Comparator.comparing(u1 -> {

                    Integer summation = 0;
                    for (NodeDTO w : successors) {
                      if (!w.equals(u1)) {
                        summation += (computeWCET(s.get(w)) - u1.getWeight()) / m;
                      }
                    }

                    return f.get(u1) + summation;

                  }))
                  .orElse(null);

          // riga 18
          tmpSet = new HashSet<>();
          tmpSet.add(currentNode);
          tmpSet.addAll(t.get(uStar));

          t.put(currentNode, tmpSet);

          // riga 19
          Integer summation = 0;
          for (NodeDTO w : successors) {
            if (uStar != null && !w.equals(uStar)) {
              summation += (computeWCET(s.get(w)) - uStar.getWeight()) / m;
            }
          }

          f.put(currentNode, currentNode.getWeight() + f.get(uStar) + summation);

        }
      }
    }
    return f.get(source);
  }
}
