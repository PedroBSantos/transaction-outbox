package br.com.pedro.web.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pedro.core.domain.*;

public interface UserJpaRepository extends JpaRepository<User, UUID>, UserRepository {

    @Override
    default void create(User user) {
        save(user);
    }

    Optional<User> findByEmail(String email);

    @Override
    default Optional<User> readUserByEmail(String email) {
        return findByEmail(email);
    }
}
