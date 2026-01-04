package ale.stefanini.taskmanager.taskservice.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.models.PaginatedResponse;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.CreateTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.DeleteTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.GetTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.UpdateTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskRepositoryPort;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskNotificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskService implements CreateTaskUseCase, GetTaskUseCase, UpdateTaskUseCase, DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final MeterRegistry meterRegistry;
    private final TaskNotificationPort taskNotificationPort;

    private Counter tasksCreatedCounter;
    private Counter tasksDeletedCounter;

    public TaskService(
            TaskRepositoryPort taskRepositoryPort,
            MeterRegistry meterRegistry,
            TaskNotificationPort taskNotificationPort) {
        this.taskRepositoryPort = taskRepositoryPort;
        this.meterRegistry = meterRegistry;
        this.taskNotificationPort = taskNotificationPort;
    }

    @PostConstruct
    public void initMetrics() {
        tasksCreatedCounter = Counter.builder("stefanini.tasks.created")
                .description("Total de tareas creadas")
                .register(meterRegistry);

        tasksDeletedCounter = Counter.builder("stefanini.tasks.deleted")
                .description("Total de tareas eliminadas")
                .register(meterRegistry);
    }

    @Override
    public Task createTask(Task task) {
        log.info("Create a new task with title : {}", task.getTitle());
        task.setId(UUID.randomUUID());
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepositoryPort.save(task);

        try {
            taskNotificationPort.notifyTaskCreated(savedTask);
            log.info("Notificación enviada a traves del puerto - ID: {}", savedTask.getId());
        } catch (Exception e) {
            log.error("Error al notificar, pero la tarea se guardó en el DB - ID: {}", savedTask.getId(), e);
        }

        this.tasksCreatedCounter.increment();
        log.info("Métrica: Tarea creada incrementada con ID: {}", savedTask.getId());
        return savedTask;
    }

    @Override
    public Optional<Task> getTask(UUID id) {
        return taskRepositoryPort.findById(id);
    }

    @Override
    public PaginatedResponse<Task> getAllTasks(int page, int size) {
        return taskRepositoryPort.findAll(page, size);
    }

    @Override
    public Optional<Task> updateTask(UUID id, Task task) {
        return taskRepositoryPort.findById(id).map(existingTask -> {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            return taskRepositoryPort.save(existingTask);
        });
    }

    @Override
    public boolean deleteTask(UUID id) {
        return taskRepositoryPort.findById(id).map(task -> {
            taskRepositoryPort.deleteById(id);

            this.tasksDeletedCounter.increment();
            log.info("Métrica: Tarea eliminada incrementada con ID: {}", id);

            return true;
        }).orElse(false);
    }

}
