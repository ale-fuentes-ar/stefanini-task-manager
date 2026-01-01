package ale.stefanini.taskmanager.taskservice.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Task {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private TaskStatus status;

    public void complete() {
        this.status = TaskStatus.COMPLETED;
    }

    public void cancel() {
        this.status = TaskStatus.CANCELLED;
    }

    public boolean isPending() {
        return TaskStatus.PENDING.equals(this.status);
    }

}
