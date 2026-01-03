package ale.stefanini.taskmanager.taskservice.domain.ports.out;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.models.PaginatedResponse;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    Task save(Task task);

    Optional<Task> findById(UUID id);

    PaginatedResponse<Task> findAll(int page, int size);

    void deleteById(UUID id);
}
