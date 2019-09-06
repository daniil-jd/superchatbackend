package ru.itpark.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itpark.dto.chat.room.UserDto;
import ru.itpark.entity.UserEntity;
import ru.itpark.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllRooms(@AuthenticationPrincipal UserEntity user) {
        return userService.getAllUsersWithoutMe(user)
                .stream()
                .map(u -> new UserDto(u.getUsername()))
                .collect(Collectors.toList());
    }
}
