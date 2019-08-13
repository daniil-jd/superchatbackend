package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.entity.UserEntity;
import ru.itpark.repository.UserRepository;

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
}
