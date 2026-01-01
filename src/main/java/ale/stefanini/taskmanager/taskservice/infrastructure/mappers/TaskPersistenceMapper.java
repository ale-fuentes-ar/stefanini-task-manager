package ale.stefanini.taskmanager.taskservice.infrastructure.mappers;

import org.springframework.stereotype.Component;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.persistence.TaskEntity;

@Component
public class TaskPersistenceMapper {

    public TaskEntity toEntity(Task domainModel) {
        return new TaskEntity(
                domainModel.getId(),
                domainModel.getTitle(),
                domainModel.getDescription(),
                domainModel.getCreatedAt(),
                domainModel.getStatus()
            );
    }

    public Task toDomain(TaskEntity entity) {
        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
