package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.dto.AuthenticationTokenRequestDto;
import ru.itpark.dto.AuthenticationTokenResponseDto;
import ru.itpark.entity.token.AuthenticationTokenEntity;
import ru.itpark.exception.BadCredentialsException;
import ru.itpark.exception.UserDoesNotExistException;
import ru.itpark.repository.AuthenticationTokenRepository;
import ru.itpark.repository.UserRepository;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationTokenRepository  authenticationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationTokenResponseDto authenticate(AuthenticationTokenRequestDto dto) {
        var userEntity = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new UserDoesNotExistException("api.exception.user.not_exist.message")
        );

        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException("api.exception.bad_credentials.message");
        }

        var token = UUID.randomUUID().toString();
        var tokenEntity = new AuthenticationTokenEntity(token, userEntity);
        authenticationTokenRepository.deleteAllByUser(userEntity);
        authenticationTokenRepository.save(tokenEntity);

        return new AuthenticationTokenResponseDto(token);
    }

}
