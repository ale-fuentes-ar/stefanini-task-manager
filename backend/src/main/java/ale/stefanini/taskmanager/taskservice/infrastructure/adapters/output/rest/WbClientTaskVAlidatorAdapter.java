package ale.stefanini.taskmanager.taskservice.infrastructure.adapters.output.rest;

import ale.stefanini.taskmanager.taskservice.domain.ports.out.ExternalValidatorPort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WbClientTaskVAlidatorAdapter implements ExternalValidatorPort {

    private final WebClient webClient;

    public WbClientTaskVAlidatorAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    @Override
    public Mono<Boolean> isValidTaskTitle(String title) {
        return this.webClient.get()
                .uri("/posts/1")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> title != null && !title.isEmpty());

    }

}
