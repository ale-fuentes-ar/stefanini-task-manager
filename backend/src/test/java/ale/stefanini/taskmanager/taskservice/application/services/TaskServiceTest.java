package ale.stefanini.taskmanager.taskservice.application.services;

import ale.stefanini.taskmanager.taskservice.domain.models.PaginatedResponse;
import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.models.TaskStatus;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskRepositoryPort;
import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskNotificationPort;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepositoryPort;
    
    @Mock
    private TaskNotificationPort taskNotificationPort;

    private MeterRegistry meterRegistry;
    private TaskService taskService;
    private Task sampleTask;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        taskService = new TaskService(taskRepositoryPort, meterRegistry, taskNotificationPort);
        
        taskService.initMetrics();

        sampleTask = Task.builder()
                .id(UUID.randomUUID())
                .title("Sample Task")
                .description("Description")
                .status(TaskStatus.PENDING)
                .build();
    }

    @Test
    void shouldCreateTaskSuccessFully() {
        // Arrange
        when(taskRepositoryPort.save(any(Task.class))).thenReturn(sampleTask);

        // Act
        Task created = taskService.createTask(sampleTask);

        // Assert
        assertNotNull(created);
        assertEquals("Sample Task", created.getTitle());
        verify(taskRepositoryPort, times(1)).save(any(Task.class));
        verify(taskNotificationPort, times(1)).notifyTaskCreated(any(Task.class));
    }

    @Test
    void shouldReturnEmptyWhenTaskNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(taskRepositoryPort.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Task> result = taskService.getTask(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnPaginatedTasks() {
        // Arrange
        int page = 0;
        int size = 5;
        PaginatedResponse<Task> mockResponse = new PaginatedResponse<>(List.of(sampleTask), 1, 1, 0, 5);
        when(taskRepositoryPort.findAll(page, size)).thenReturn(mockResponse);

        // Act
        PaginatedResponse<Task> result = taskService.getAllTasks(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getCurrentPage());
        verify(taskRepositoryPort, times(1)).findAll(page, size);
    }

}