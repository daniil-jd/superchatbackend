package ru.itpark.dto.chat.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private long id;
    // private String command; // ваш собственный протокол
    private String authorName;
    private String roomName;
    private String message;
    private LocalDateTime created;
    private String status;
}
