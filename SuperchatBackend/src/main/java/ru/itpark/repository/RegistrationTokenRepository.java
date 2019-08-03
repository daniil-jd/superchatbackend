package ru.itpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itpark.entity.token.RegistrationTokenEntity;

import java.util.List;

public interface RegistrationTokenRepository extends JpaRepository<RegistrationTokenEntity, String> {

    List<RegistrationTokenEntity> findAllByUserId(long id);
}
