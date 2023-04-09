package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.repositories.GraphRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class GraphService {

  private final GraphRepository graphRepository;

  @Autowired
  public GraphService(GraphRepository graphRepository) {
    this.graphRepository = graphRepository;
  }

  /**
   * Function that gives a topological sort for the given graph
   *
   * @param graph: given graph
   * @return the list of nodes that compose the desired topological sort
   */
  public List<NodeDTO> topologicalSort(GraphDTO graph) {

    // initialization
    Stack<NodeDTO> stack = new Stack<>();
    graph.getNodes().forEach(n -> n.setVisited(false));

    // graph.getNodes().forEach(n -> topologicalSortUtil(graph, n, stack));
    for (NodeDTO n : graph.getNodes()) {
      if (!n.isVisited()) {
        topologicalSortUtil(graph, n, stack);
      }
    }

    List<NodeDTO> result = new ArrayList<>(graph.getNodes().size());

    while (!stack.empty()) {
      result.add(stack.pop());
    }

    return result;
  }

  /**
   * Utils recursive function for topological sort
   * @param graph: given graph
   * @param node: current node
   * @param stack: stack to push sorted nodes
   */
  private void topologicalSortUtil(GraphDTO graph, NodeDTO node, Stack<NodeDTO> stack) {

    node.setVisited(true);

    graph.getAdjNodes(node).forEach(n -> {
      if (!n.isVisited()) {
        topologicalSortUtil(graph, n, stack);
      }
    });

    stack.push(node);

  }

  public Graph insertNewGraph(Graph graph){
    return graphRepository.save(graph);
  }

}
