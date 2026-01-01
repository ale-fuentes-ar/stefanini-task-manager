package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.input.rest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ale.stefanini.taskmanager.taskservice.application.dtos.TaskRequest;
import ale.stefanini.taskmanager.taskservice.application.dtos.TaskResponse;
import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.CreateTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.DeleteTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.GetTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.UpdateTaskUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .build();

        Task createTask = createTaskUseCase.createTask(task);
        return new ResponseEntity<>(mapToResponse(createTask), HttpStatus.CREATED);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return getTaskUseCase.getAllTasks().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
        return getTaskUseCase.getTask(id)
                .map(task -> ResponseEntity.ok(mapToResponse(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody TaskRequest request) {

        Task taskUpdate = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(request.status())
                .build();

        return updateTaskUseCase.updateTask(id, taskUpdate)
                .map(task -> ResponseEntity.ok(mapToResponse(task)))
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        if (deleteTaskUseCase.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getStatus());
    }
}
