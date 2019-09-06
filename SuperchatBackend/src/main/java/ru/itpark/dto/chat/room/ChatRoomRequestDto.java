package ru.itpark.dto.chat.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itpark.validation.ChatMembers;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestDto {
    @NotNull
    private String name;
    @NotNull
    @ChatMembers
    private Set<UserDto> members = new HashSet<>();
}
