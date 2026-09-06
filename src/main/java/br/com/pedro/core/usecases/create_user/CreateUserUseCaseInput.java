package br.com.pedro.core.usecases.create_user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserUseCaseInput(
        @NotBlank @Size(max = 150, min = 2) String name,
        @NotBlank @Email String email) {

}
