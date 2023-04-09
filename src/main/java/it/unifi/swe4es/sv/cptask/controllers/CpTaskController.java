package it.unifi.swe4es.sv.cptask.controllers;

import it.unifi.swe4es.sv.cptask.dto.BaseResponseDTO;
import it.unifi.swe4es.sv.cptask.dto.NodeDTO;
import it.unifi.swe4es.sv.cptask.services.CpTaskService;
import it.unifi.swe4es.sv.cptask.services.DemoGraphService;
import it.unifi.swe4es.sv.cptask.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/cp-task")
public class CpTaskController {

  private final GraphService graphService;
  private final CpTaskService cpTaskService;
  private final DemoGraphService demoGraphService;
  @Autowired
  public CpTaskController(GraphService graphService,
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

  @GetMapping("/max-volume-demo")
  public ResponseEntity<BaseResponseDTO> maxVolumeDemo() {
    List<String> range = Stream.of("set1", "set2", "set3").toList();

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
}
