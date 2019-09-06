package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.entity.UserEntity;
import ru.itpark.exception.UserDoesNotExistException;
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
            throw new UserDoesNotExistException("api.exception.user.not_exist.message");
        }
        return userOptional.get();
    }

    public List<UserEntity> getAllUsersWithoutMe(UserEntity me) {
        var users = userRepository.findAll();
        users.remove(me);
        return users;
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void scheduledDeleteChatUser() {
        userRepository.deleteUserEntitiesByTime();
    }
}
