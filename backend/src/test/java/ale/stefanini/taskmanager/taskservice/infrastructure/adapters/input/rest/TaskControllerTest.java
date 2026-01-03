package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ale.stefanini.taskmanager.taskservice.application.dtos.TaskRequest;
import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.models.TaskStatus;
import ale.stefanini.taskmanager.taskservice.domain.models.PaginatedResponse;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.CreateTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.DeleteTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.GetTaskUseCase;
import ale.stefanini.taskmanager.taskservice.domain.ports.in.UpdateTaskUseCase;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTaskUseCase createTaskUseCase;

    @MockitoBean
    private GetTaskUseCase getTaskUseCase;

    @MockitoBean
    private UpdateTaskUseCase updateTaskUseCase;

    @MockitoBean
    private DeleteTaskUseCase deleteTaskUseCase;

    @Test
    void shouldReturnCreatedWhenPostingValidTask() throws Exception {
        // Arrange
        TaskRequest request = new TaskRequest("Test Title", "Description", TaskStatus.PENDING);
        Task mockTask = Task.builder()
                .id(UUID.randomUUID())
                .title("Test Title")
                .status(TaskStatus.PENDING)
                .build();

        when(createTaskUseCase.createTask(any(Task.class))).thenReturn(mockTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.status").value("PENDING"));

    }

    @Test
    void shouldReturnBadRequestWhenTitleIsEmpty() throws Exception {
        // Arrange
        TaskRequest invalidRequest = new TaskRequest("", "Not title", TaskStatus.PENDING);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
    }

    @Test
    void shouldReturnPaginatedResponse() throws Exception {
        // Arrange
        List<Task> tasks = List.of(Task.builder().title("Test").build());
        PaginatedResponse<Task> pagedResponse = new PaginatedResponse<>(tasks, 1, 1, 0, 5);

        when(getTaskUseCase.getAllTasks(0, 5)).thenReturn(pagedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/tasks?page=0&size=5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.pageSize").value(5));
    }

}
