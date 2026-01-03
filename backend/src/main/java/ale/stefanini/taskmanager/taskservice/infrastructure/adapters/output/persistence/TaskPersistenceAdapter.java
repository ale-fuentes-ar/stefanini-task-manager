package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page; 
import java.util.List;
import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskRepositoryPort;
import ale.stefanini.taskmanager.taskservice.infrastructure.mappers.TaskPersistenceMapper;
import ale.stefanini.taskmanager.taskservice.domain.models.PaginatedResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements TaskRepositoryPort {

    private final JpaTaskRepository jpaTaskRepository;
    private final TaskPersistenceMapper mapper;

    @Override
    public Task save(Task task) {
        TaskEntity entity = mapper.toEntity(task);
        TaskEntity savedEntity = jpaTaskRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaTaskRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public PaginatedResponse<Task> findAll(int page, int size) {
        var pageable = PageRequest.of(page, size);
        Page<TaskEntity> entityPage = jpaTaskRepository.findAll(pageable);
        List<Task> domainTasks = entityPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return new PaginatedResponse<>(
                domainTasks,
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.getNumber(),
                entityPage.getSize()
        );
    }

    @Override
    public void deleteById(UUID id) {
        jpaTaskRepository.deleteById(id);
    }

}
