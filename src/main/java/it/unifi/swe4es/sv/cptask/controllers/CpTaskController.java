package it.unifi.swe4es.sv.cptask.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unifi.swe4es.sv.cptask.dto.BaseResponseDTO;
import it.unifi.swe4es.sv.cptask.dto.GraphDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.mappers.GraphMapper;
import it.unifi.swe4es.sv.cptask.services.CpTaskService;
import it.unifi.swe4es.sv.cptask.services.DemoGraphService;
import it.unifi.swe4es.sv.cptask.services.GraphV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/cp-task")
public class CpTaskController {

  private final GraphV2Service graphService;
  private final CpTaskService cpTaskService;
  private final DemoGraphService demoGraphService;
  @Autowired
  public CpTaskController(GraphV2Service graphService,
                          CpTaskService cpTaskService,
                          DemoGraphService demoGraphService) {

    this.graphService = graphService;
    this.cpTaskService = cpTaskService;
    this.demoGraphService = demoGraphService;
  }

  @GetMapping("/top-sort-demo")
  public ResponseEntity<List<NodeDTO>> topSortDemo() {

    List<NodeDTO> topologicalSort = new ArrayList<>(graphService.topologicalSort(demoGraphService.getDemoGraph()));

    return ResponseEntity.ok()
            .body(topologicalSort);
  }

  @GetMapping("/top-sort/{id}")
  public ResponseEntity<List<NodeDTO>> topSort(@PathVariable Long id) {
    GraphDTO graphDTO = GraphMapper.INSTANCE
            .toDTO(graphService.getGraph(id));

    List<NodeDTO> topologicalSort = new ArrayList<>(graphService.topologicalSort(graphDTO));

    return ResponseEntity.ok()
            .body(topologicalSort);
  }

  @GetMapping("/max-volume-demo")
  public ResponseEntity<BaseResponseDTO> maxVolumeDemo() {
    List<String> range = Stream.of("set1", "set2", "set3").collect(Collectors.toList());

    Set<NodeDTO> set1 = demoGraphService.getDemoNodes("v1", "v2", "v3").collect(Collectors.toSet());
    Set<NodeDTO> set2 = demoGraphService.getDemoNodes("v4", "v5", "v6").collect(Collectors.toSet());
    Set<NodeDTO> set3 = demoGraphService.getDemoNodes("v7", "v8", "v9").collect(Collectors.toSet());

    Map<String, Set<NodeDTO>> setMap = new HashMap<>() {{
      put("set1", set1);
      put("set2", set2);
      put("set3", set3);
    }};

    String maxVolume = range.stream()
            .max(Comparator.comparing(n -> cpTaskService.computeWCET(setMap.get(n))))
            // .map(NodeMapper.INSTANCE::toDTO)
            .orElse(null);

    return ResponseEntity.ok()
            .body(new BaseResponseDTO(maxVolume));
  }

  @GetMapping("/worst-case-workload-demo")
  public ResponseEntity<BaseResponseDTO> worstCaseWorkloadDemo() {
    final Integer worstCaseWorkload = cpTaskService.computeWorstCaseWorkload(demoGraphService.getDemoGraph());

    return ResponseEntity.ok()
            .body(new BaseResponseDTO(Integer.toString(worstCaseWorkload)));
  }

  @GetMapping("/worst-case-workload/{id}")
  public ResponseEntity<BaseResponseDTO> worstCaseWorkload(@PathVariable Long id) {
    GraphDTO graphDTO = GraphMapper.INSTANCE
            .toDTO(graphService.getGraph(id));

    final Integer worstCaseWorkload = cpTaskService.computeWorstCaseWorkload(graphDTO);

    return ResponseEntity.ok()
            .body(new BaseResponseDTO(Integer.toString(worstCaseWorkload)));
  }

  @GetMapping("/zk-bound-demo")
  public ResponseEntity<BaseResponseDTO> zkBuondDemo() {
    Double zkBuond = cpTaskService.computeZKBuond(demoGraphService.getDemoGraph(), 4);

    return ResponseEntity.ok()
            .body(new BaseResponseDTO(Double.toString(zkBuond)));
  }

  @PostMapping("/zk-bound/{id}")
  public ResponseEntity<BaseResponseDTO> zkBuond(@PathVariable Long id,
                                                 @RequestParam(required = false) String m) {

    GraphDTO graphDTO = GraphMapper.INSTANCE
            .toDTO(graphService.getGraph(id));

    Integer processors = Optional.of(Integer.parseInt(m)).orElse(0) != 0 ? Integer.parseInt(m) : 4;

    Double zkBuond = cpTaskService.computeZKBuond(graphDTO, processors);

    return ResponseEntity.ok()
            .body(new BaseResponseDTO(Double.toString(zkBuond)));
  }

  @GetMapping("/demo-graph")
  @ResponseBody
  public ResponseEntity<String> getDemoGraph() throws JsonProcessingException {
    String response =  new ObjectMapper().writeValueAsString(demoGraphService.getDemoGraph());

    return ResponseEntity.ok()
            .body(response);
  }
}
