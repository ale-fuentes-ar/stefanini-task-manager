package ale.stefanini.taskmanager.taskservice.domain.ports.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.models.PaginatedResponse;

public interface GetTaskUseCase {
    Optional<Task> getTask(UUID id);

    PaginatedResponse<Task> getAllTasks(int page, int size);
}
