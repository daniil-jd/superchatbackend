package ru.itpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itpark.entity.chat.MessageEntity;
import ru.itpark.entity.chat.RoomEntity;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findAllByRoom(RoomEntity room);
}
