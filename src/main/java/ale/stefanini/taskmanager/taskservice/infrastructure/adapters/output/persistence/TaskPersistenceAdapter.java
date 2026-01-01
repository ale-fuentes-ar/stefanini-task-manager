package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskRepositoryPort;
import ale.stefanini.taskmanager.taskservice.infrastructure.mappers.TaskPersistenceMapper;
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
    public List<Task> findAll() {
        return jpaTaskRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaTaskRepository.deleteById(id);
    }

}
