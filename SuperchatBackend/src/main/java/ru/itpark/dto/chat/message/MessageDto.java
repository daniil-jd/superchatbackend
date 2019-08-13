package ru.itpark.dto.chat.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private long id;
    private String authorName;
    private String roomName;
    private String message;
    private String created;
    private String status;
}
