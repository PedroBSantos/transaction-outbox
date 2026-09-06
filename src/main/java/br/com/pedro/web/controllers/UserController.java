package br.com.pedro.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedro.core.usecases.create_user.CreateUserUseCase;
import br.com.pedro.core.usecases.create_user.CreateUserUseCaseInput;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(@Autowired CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserUseCaseInput input) {
        var output = this.createUserUseCase.execute(input);
        return output.fold(
                error -> ResponseEntity.status(HttpStatus.CONFLICT).body(error),
                success -> ResponseEntity.noContent().build());
    }
}
