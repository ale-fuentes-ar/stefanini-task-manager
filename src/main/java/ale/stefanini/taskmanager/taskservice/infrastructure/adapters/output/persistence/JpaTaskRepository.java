package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, UUID> {

}
