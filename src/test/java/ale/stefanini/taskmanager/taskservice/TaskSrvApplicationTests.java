package ale.stefanini.taskmanager.taskservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ale.stefanini.taskmanager.taskservice.domain.ports.out.TaskRepositoryPort;

@SpringBootTest
@ActiveProfiles("TEST")
class TaskSrvApplicationTests {

	@MockBean
	TaskRepositoryPort taskRepositoryPort;

	@Test
	void contextLoads() {
	}

}
