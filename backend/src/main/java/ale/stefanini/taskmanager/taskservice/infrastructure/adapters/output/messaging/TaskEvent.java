package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.messaging;

import java.util.UUID;
import java.time.LocalDateTime;

public record TaskEvent(
        UUID id,
        String title,
        String status,
        LocalDateTime timestamp) {
}
