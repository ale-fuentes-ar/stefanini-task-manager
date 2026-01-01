package ale.stefanini.taskmanager.taskservice.domain.ports.out;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {
    Task save(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findAll();
    void deleteById(UUID id);
}
