package ru.itpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itpark.entity.token.RegistrationTokenEntity;

import java.util.List;

public interface RegistrationTokenRepository extends JpaRepository<RegistrationTokenEntity, String> {
    List<RegistrationTokenEntity> findAllByUserId(long id);

    @Modifying
    @Query(value = "DELETE FROM ITPARK.REGISTRATION_TOKEN WHERE datediff(minute, CREATED, CURRENT_TIMESTAMP()) >= 15;", nativeQuery = true)
    void deleteRegistrationTokenEntityByTime();
}
