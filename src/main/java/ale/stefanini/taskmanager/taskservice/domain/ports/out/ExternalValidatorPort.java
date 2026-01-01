package ale.stefanini.taskmanager.taskservice.domain.ports.out;

import reactor.core.publisher.Mono;

public interface ExternalValidatorPort {

    Mono<Boolean> isValidTaskTitle(String title);
}
