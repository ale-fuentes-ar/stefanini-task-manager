package ale.stefanini.taskmanager.taskservice.domain.ports.out;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;

public interface TaskNotificationPort {
    void notifyTaskCreated(Task task);
}