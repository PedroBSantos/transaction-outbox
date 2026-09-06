package br.com.pedro.core.usecases.create_user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pedro.core.domain.*;
import br.com.pedro.core.usecases.UseCaseError;
import br.com.pedro.core.usecases.UseCaseErrorType;
import io.vavr.control.Either;
import tools.jackson.databind.ObjectMapper;

@Service
public class CreateUserUseCase {

    private final UserRepository users;
    private final EventRepository events;
    private final ObjectMapper mapper;
    private final Logger logger;

    public CreateUserUseCase(@Autowired UserRepository users,
            @Autowired EventRepository events) {
        this.users = users;
        this.events = events;
        this.mapper = new ObjectMapper();
        this.logger = LoggerFactory.getLogger(CreateUserUseCase.class);
    }

    @Transactional(readOnly = false)
    public Either<UseCaseError, Void> execute(CreateUserUseCaseInput input) {
        this.logger.info("Creating user with name: {} and email: {}",
                input.name(), input.email());
        this.logger.info("Checking if user with email {} already exists",
                input.email());
        var optionalUserWithEmail = users.readUserByEmail(input.email());
        if (optionalUserWithEmail.isPresent()) {
            this.logger.error("User with email {} already exists", input.email());
            return Either.left(new UseCaseError(UseCaseErrorType.ENTITY_ALREADY_EXISTS,
                    List.of("User with email " + input.email() + " already exists")));
        }
        this.logger.info("User with email {} does not exist, creating new user",
                input.email());
        var user = new User(UUID.randomUUID(), input.name(), input.email(),
                LocalDateTime.now());
        this.users.create(user);
        var eventContent = this.mapper.writeValueAsString(new UserCreatedEvent(
                user.getId(), user.getName(), user.getEmail(), user.getCreatedAt()));
        var event = new Event("events:user-created", eventContent, LocalDateTime.now());
        this.events.create(event);
        return Either.right((Void) null);
    }
}
