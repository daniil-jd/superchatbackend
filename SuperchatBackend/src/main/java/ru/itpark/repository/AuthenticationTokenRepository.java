package ru.itpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itpark.entity.UserEntity;
import ru.itpark.entity.token.AuthenticationTokenEntity;

public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationTokenEntity, String> {
    void deleteAllByUser(UserEntity user);
}
