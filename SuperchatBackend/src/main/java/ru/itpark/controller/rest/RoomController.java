package ru.itpark.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.itpark.dto.chat.room.ChatRoomRequestDto;
import ru.itpark.dto.chat.room.RoomsResponseDto;
import ru.itpark.entity.UserEntity;
import ru.itpark.service.RoomsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomsService roomsService;

    @GetMapping
    public List<RoomsResponseDto> getAllRooms(@AuthenticationPrincipal UserEntity user) {
        return roomsService.getAllMemberRooms(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createRoom(@Valid @RequestBody ChatRoomRequestDto chatRoomRequestDto, @AuthenticationPrincipal UserEntity user) {
        roomsService.createRoom(chatRoomRequestDto, user);
    }
}
