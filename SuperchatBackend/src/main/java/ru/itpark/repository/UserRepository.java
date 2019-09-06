package ru.itpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.itpark.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM ITPARK.CHAT_USER WHERE enabled = false AND datediff(minute, CREATED, CURRENT_TIMESTAMP()) >= 15;", nativeQuery = true)
    void deleteUserEntitiesByTime();
}
