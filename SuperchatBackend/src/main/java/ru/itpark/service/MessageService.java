package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.dto.chat.message.MessageDto;
import ru.itpark.entity.chat.MessageEntity;
import ru.itpark.entity.chat.RoomEntity;
import ru.itpark.repository.MessageRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public void save(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
    }

    public List<MessageDto> getAllMessagesByRoom(RoomEntity room) {
        var messages = messageRepository.findAllByRoom(room);

        return messages
                .stream()
                .map(m -> new MessageDto(
                        m.getId(),
                        m.getAuthor().getUsername(),
                        m.getRoom().getName(),
                        m.getContent(),
                        m.getCreated().toString(),
                        m.getStatus().toString())
                ).collect(Collectors.toList())
                ;
    }
}
