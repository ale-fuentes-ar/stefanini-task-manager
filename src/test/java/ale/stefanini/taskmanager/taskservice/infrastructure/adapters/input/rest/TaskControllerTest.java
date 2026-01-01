package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ale.stefanini.taskmanager.taskservice.application.dtos.TaskRequest;
import ale.stefanini.taskmanager.taskservice.domain.models.Task;
import ale.stefanini.taskmanager.taskservice.domain.models.TaskStatus;
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
    private ObjectMapper obkjectMapper;
    @MockBean
    private CreateTaskUseCase createTaskUseCase;

    @MockBean
    private GetTaskUseCase getTaskUseCase;

    @MockBean
    private UpdateTaskUseCase updateTaskUseCase;

    @MockBean
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
                .content(obkjectMapper.writeValueAsString(request)))
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
                .content(obkjectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title is required"));
    }

}
