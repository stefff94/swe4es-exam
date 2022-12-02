package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class GraphService {

  /**
   * Function that gives a topological sort for the given graph
   *
   * @param graph: given graph
   * @return the list of nodes that compose the desired topological sort
   */
  public List<Node> topologicalSort(Graph graph) {

    // initialization
    Stack<Node> stack = new Stack<>();
    graph.getNodes().forEach(n -> n.setVisited(false));

    // graph.getNodes().forEach(n -> topologicalSortUtil(graph, n, stack));
    for (Node n : graph.getNodes()) {
      if (!n.isVisited()) {
        topologicalSortUtil(graph, n, stack);
      }
    }

    List<Node> result = new ArrayList<>(graph.getNodes().size());

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
  private void topologicalSortUtil(Graph graph, Node node, Stack<Node> stack) {

    node.setVisited(true);

    graph.getAdjNodes(node).forEach(n -> {
      if (!n.isVisited()) {
        topologicalSortUtil(graph, n, stack);
      }
    });

    stack.push(node);

  }


}
