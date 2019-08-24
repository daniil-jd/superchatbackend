package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.dto.chat.room.UserDto;
import ru.itpark.entity.UserEntity;
import ru.itpark.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity findUserEntityByName(String name) {
        var userOptional = userRepository.findByUsername(name);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(name);
        }
        return userOptional.get();
    }

    public List<UserEntity> getAllUsersWithoutMe(UserEntity me) {
        var users = userRepository.findAll();
        users.remove(me);
        return users;
    }
}
