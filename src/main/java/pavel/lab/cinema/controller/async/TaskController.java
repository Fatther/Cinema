package pavel.lab.cinema.controller.async;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pavel.lab.cinema.service.async.TaskService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/report")
    public ResponseEntity<Map<String, String>> startReport() {
        String taskId = UUID.randomUUID().toString();
        taskService.generateReport(taskId);
        return ResponseEntity.accepted().body(Map.of("TaskId", taskId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Map<String, String>> getStatus(@PathVariable String taskId) {
        String status = taskService.getStatus(taskId);
        return ResponseEntity.ok().body(Map.of("Status", status));
    }
}
