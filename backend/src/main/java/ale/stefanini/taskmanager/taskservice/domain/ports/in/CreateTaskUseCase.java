package ale.stefanini.taskmanager.taskservice.domain.ports.in;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;

public interface CreateTaskUseCase {
    Task createTask(Task task);
}
