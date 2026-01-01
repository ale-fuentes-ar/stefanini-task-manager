package ale.stefanini.taskmanager.taskservice.domain.ports.in;

import java.util.Optional;
import java.util.UUID;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;

public interface UpdateTaskUseCase {
    Optional<Task> updateTask(UUID id, Task task);
}
