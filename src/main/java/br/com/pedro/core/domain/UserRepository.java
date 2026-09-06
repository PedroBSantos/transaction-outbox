package br.com.pedro.core.domain;

import java.util.Optional;

public interface UserRepository {

    void create(User user);

    Optional<User> readUserByEmail(String email);
}
