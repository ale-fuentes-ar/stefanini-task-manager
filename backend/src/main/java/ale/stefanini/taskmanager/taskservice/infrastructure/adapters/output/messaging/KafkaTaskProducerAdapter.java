package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.messaging;

import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskNotificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskProducerAdapter implements TaskNotificationPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "task-events";
    
    @Override
    public void notifyTaskCreated(Task task) {
        TaskEvent event = new TaskEvent(
                task.getId(),
                task.getTitle(),
                task.getStatus().name(),
                LocalDateTime.now()
        );

        log.info("Publicando evento en kafka: {}", event.title());
        kafkaTemplate.send(TOPIC, event);
    }

}