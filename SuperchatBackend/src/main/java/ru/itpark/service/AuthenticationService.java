package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.dto.AuthenticationTokenRequestDto;
import ru.itpark.dto.AuthenticationTokenResponseDto;
import ru.itpark.entity.token.AuthenticationTokenEntity;
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
        var userEntity = userRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new UsernameNotFoundException(dto.getUsername()));

        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) {
            throw new BadCredentialsException(dto.getUsername());
        }

        var token = UUID.randomUUID().toString();
        var tokenEntity = new AuthenticationTokenEntity(token, userEntity);
        authenticationTokenRepository.deleteAllByUser(userEntity);
        authenticationTokenRepository.save(tokenEntity);

        return new AuthenticationTokenResponseDto(token);
    }

}
