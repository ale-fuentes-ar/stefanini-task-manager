package ale.stefanini.taskmanager.taskservice.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.CreateTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.DeleteTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.GetTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.UpdateTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService implements CreateTaskUseCase, GetTaskUseCase, UpdateTaskUseCase, DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    @Override
    public Task createTask(Task task) {
        log.info("Create a new task with title : {}", task.getTitle());
        task.setId(UUID.randomUUID());
        task.setCreatedAt(LocalDateTime.now());

        return taskRepositoryPort.save(task);
    }

    @Override
    public Optional<Task> getTask(UUID id) {
        return taskRepositoryPort.findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepositoryPort.findAll();
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
            return true;
        }).orElse(false);
    }

}
