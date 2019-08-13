package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.dto.chat.room.ChatRoomRequestDto;
import ru.itpark.dto.chat.room.UserDto;
import ru.itpark.dto.chat.room.RoomsResponseDto;
import ru.itpark.entity.UserEntity;
import ru.itpark.entity.chat.RoomEntity;
import ru.itpark.exception.ChatRoomAlreadyExist;
import ru.itpark.exception.EmptyChatMembersException;
import ru.itpark.exception.RoomNotFindException;
import ru.itpark.exception.UserDoesNotExist;
import ru.itpark.repository.RoomsRepository;
import ru.itpark.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomsService {
    private final RoomsRepository roomsRepository;
    private final UserRepository userRepository;

    public List<RoomsResponseDto> getAllMemberRooms(UserEntity userEntity) {
        return roomsRepository.findAllByUsers(userEntity).stream()
                .map(r -> new RoomsResponseDto(r.getName(), r.getIcon(), new UserDto(r.getCreator().getUsername()), r.getUsersDto()))
                .collect(Collectors.toList());
    }

    public void createRoom(ChatRoomRequestDto chatRoomRequestDto, UserEntity creator) {
        if (roomsRepository.findByName(chatRoomRequestDto.getName()).isPresent()) {
            throw new ChatRoomAlreadyExist(String.format("Chat room with name '%s' already exist", chatRoomRequestDto.getName()));
        }

        List<UserEntity> users = new ArrayList<>();
        if (chatRoomRequestDto.getMembers() != null && chatRoomRequestDto.getMembers().size() > 0) {
            for (UserDto member : chatRoomRequestDto.getMembers()) {
                var userEntity = userRepository.findByUsername(member.getUsername());
                if (userEntity.isPresent()) {
                    users.add(userEntity.get());
                } else {
                    throw new UserDoesNotExist(member.getUsername());
                }
            }
        } else {
            throw new EmptyChatMembersException();
        }

        roomsRepository.save(new RoomEntity(0, chatRoomRequestDto.getName(), null, creator, users));
    }

    public List<UserEntity> findUsersInRoomByRoomName(String roomName) {
        var room = roomsRepository.findByName(roomName);
        if (!room.isPresent()) {
            throw new RoomNotFindException();
        }
        return new ArrayList<>(room.get().getUsers());
    }

    public RoomEntity findByRoomName(String name) {
        var room = roomsRepository.findByName(name);
        if (!room.isPresent()) {
            throw new RoomNotFindException();
        }
        return room.get();
    }


}
