package ru.itpark.dto.chat.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomsResponseDto {
    private String name;
    private String icon;
    private UserDto creator;
    private List<UserDto> users = new ArrayList<>();
}
