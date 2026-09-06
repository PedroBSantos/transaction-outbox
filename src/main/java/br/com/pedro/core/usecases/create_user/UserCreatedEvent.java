package br.com.pedro.core.usecases.create_user;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        String name,
        String email,
        LocalDateTime createdAt) {
}
