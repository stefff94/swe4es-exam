package it.unifi.swe4es.sv.cptask.services;

import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.mappers.GraphMapper;
import it.unifi.swe4es.sv.cptask.models.Graph;
import it.unifi.swe4es.sv.cptask.models.Node;
import it.unifi.swe4es.sv.cptask.repositories.GraphRepository;
import it.unifi.swe4es.sv.cptask.repositories.NodeRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class GraphService {

  private final GraphRepository graphRepository;

  private final NodeService nodeService;

  private final GraphPathService graphPathService;

  @Autowired
  public GraphService(GraphRepository graphRepository, NodeService nodeService, GraphPathService graphPathService) {
    this.graphRepository = graphRepository;
    this.nodeService = nodeService;
    this.graphPathService = graphPathService;
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

  public Graph getGraph(Long id) {
    return graphRepository.findById(id).orElse(null);
  }

  public Graph insertNewGraph(Graph graph) {
    Graph savedGraph = graphRepository.save(graph);

    graph.getGraphPath().forEach(gp-> gp.setGraph(savedGraph));

    graph.getGraphPath().forEach(graphPathService::insertNewGraphPath);

    return savedGraph;
  }

}
