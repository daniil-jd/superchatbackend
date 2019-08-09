package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.dto.chat.room.ChatRoomRequestDto;
import ru.itpark.dto.chat.room.MemberDto;
import ru.itpark.dto.chat.room.RoomsResponseDto;
import ru.itpark.entity.UserEntity;
import ru.itpark.entity.chat.MemberEntity;
import ru.itpark.entity.chat.RoomEntity;
import ru.itpark.exception.ChatRoomAlreadyExist;
import ru.itpark.exception.EmptyChatMembersException;
import ru.itpark.exception.RoomNotFindException;
import ru.itpark.exception.UserDoesNotExist;
import ru.itpark.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final RoomsRepository roomsRepository;
    private final UserRepository userRepository;

    public List<RoomsResponseDto> getAllMemberRooms(UserEntity userEntity) {
        var memberEntity = memberRepository.findByChatUser(userEntity);
        return roomsRepository.findAllByMembers(memberEntity).stream()
                .map(r -> new RoomsResponseDto(r.getName(), r.getIcon(), new MemberDto(r.getCreator().getChatUser().getUsername()), r.getMembersDto()))
                .collect(Collectors.toList());
    }

    public void createRoom(ChatRoomRequestDto chatRoomRequestDto, UserEntity creator) {
        if (roomsRepository.findByName(chatRoomRequestDto.getName()).isPresent()) {
            throw new ChatRoomAlreadyExist(String.format("Chat room with name '%s' already exist", chatRoomRequestDto.getName()));
        }

        List<MemberEntity> members = new ArrayList<>();
        if (chatRoomRequestDto.getMembers() != null && chatRoomRequestDto.getMembers().size() > 0) {
            for (MemberDto member : chatRoomRequestDto.getMembers()) {
                var userEntity = userRepository.findByUsername(member.getUsername());
                if (userEntity.isPresent()) {
                    members.add(memberRepository.findByChatUser(userEntity.get()));
                } else {
                    throw new UserDoesNotExist(member.getUsername());
                }
            }
        } else {
            throw new EmptyChatMembersException();
        }

        var memberEntity = memberRepository.findByChatUser(creator);

        roomsRepository.save(new RoomEntity(0, chatRoomRequestDto.getName(), null, memberEntity, members));
    }

    public Optional<RoomEntity> findRoomByName(String roomName) {
        return roomsRepository.findByName(roomName);
    }
}
