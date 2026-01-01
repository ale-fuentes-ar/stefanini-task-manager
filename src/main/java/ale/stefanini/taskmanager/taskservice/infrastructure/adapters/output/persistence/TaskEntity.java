package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import ale.stefanini.taskmanager.taskservice.domain.models.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    @Id
    private UUID id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
}
