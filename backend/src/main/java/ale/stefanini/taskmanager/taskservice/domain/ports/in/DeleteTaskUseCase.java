package ale.stefanini.taskmanager.taskservice.domain.ports.in;

import java.util.UUID;

public interface DeleteTaskUseCase {

    boolean deleteTask(UUID id);
}
