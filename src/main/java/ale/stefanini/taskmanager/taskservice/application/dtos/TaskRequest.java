package ale.stefanini.taskmanager.taskservice.application.dtos;

import ale.stefanini.taskmanager.taskservice.domain.models.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(
        @NotBlank(message = "Title is required") String title,
        String description,
        @NotNull(message = "Status is required") TaskStatus status) {
}
