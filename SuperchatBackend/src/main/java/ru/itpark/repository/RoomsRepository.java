package ru.itpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itpark.entity.UserEntity;
import ru.itpark.entity.chat.RoomEntity;

import java.util.List;
import java.util.Optional;

public interface RoomsRepository extends JpaRepository<RoomEntity, Long> {
    Optional<RoomEntity> findByName(String name);
    List<RoomEntity> findAllByUsers(UserEntity member);
}
