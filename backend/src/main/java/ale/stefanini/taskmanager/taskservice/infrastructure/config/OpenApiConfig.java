package ale.stefanini.taskmanager.taskservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stefaini Task Manager API")
                        .version("1.0")
                        .description(
                                "API RESTful para gestion de tareas (To-Do List) desarrollada con Arquitectur Hexagonal y Java 21.")
                        .contact(new Contact()
                                .name("Raul Alejandro Fuentes")
                                .email("ale.fuentes@stefanini.com")));
    }
}
