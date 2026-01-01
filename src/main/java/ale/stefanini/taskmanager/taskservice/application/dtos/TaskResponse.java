package ale.stefanini.taskmanager.taskservice.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import ale.stefanini.taskmanager.taskservice.domain.models.TaskStatus;

public record TaskResponse(
    UUID id,
    String title,
    String description,
    LocalDateTime createdAt,
    TaskStatus status
) 
{}
