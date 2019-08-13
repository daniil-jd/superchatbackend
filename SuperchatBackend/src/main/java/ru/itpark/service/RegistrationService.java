package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itpark.dto.AuthenticationTokenResponseDto;
import ru.itpark.dto.RegistrationRequestDto;
import ru.itpark.entity.token.AuthenticationTokenEntity;
import ru.itpark.entity.token.RegistrationTokenEntity;
import ru.itpark.entity.UserEntity;
import ru.itpark.exception.*;
import ru.itpark.repository.AuthenticationTokenRepository;
import ru.itpark.repository.RegistrationTokenRepository;
import ru.itpark.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final DefaultMailService mailService;
    private final AuthenticationTokenRepository authenticationTokenRepository;

    private final String ROLE_USER = "ROLE_USER";

    public void register(RegistrationRequestDto dto) {
        var tokenValue = UUID.randomUUID().toString();
        var registrationTime = LocalDateTime.now();
        var userOptional = userRepository.findByUsername(dto.getUsername());

        if (userOptional.isEmpty()) {
            var user = new UserEntity(
                    0L,
                    dto.getUsername(),
                    dto.getUsername(),
                    passwordEncoder.encode(dto.getPassword()),
                    List.of(new SimpleGrantedAuthority(ROLE_USER)),
                    true,
                    true,
                    true,
                    false
            );

            var token = new RegistrationTokenEntity(
                    tokenValue,
                    user,
                    registrationTime
            );
            registrationTokenRepository.save(token);

            //mail send to user
            mailService.sendRegistrationToken("mailfrreg@yandex.ru", user.getUsername(), tokenValue);
        } else {
            if (userOptional.get().isEnabled()) {
                throw new UsernameAlreadyExistsException(dto.getUsername());
            }

            if (registrationTokenRepository.findAllByUserId(userOptional.get().getId()).size() >= 3) {
                throw new TooManyRegistrationRequestsException();
            }

            var token = new RegistrationTokenEntity(
                    tokenValue,
                    userOptional.get(),
                    registrationTime
            );

            registrationTokenRepository.save(token);

            //mail token send
            mailService.sendRegistrationToken("mailfrreg@yandex.ru", userOptional.get().getUsername(), tokenValue);
        }


    }

    public AuthenticationTokenResponseDto confirm(String tokenValue) {
        var token = registrationTokenRepository.findById(tokenValue);

        if (token.isEmpty()) {
            throw new AuthenticationTokenNotFoundException();
        }

        var user = token.get().getUser();
        if (registrationTokenRepository.findAllByUserId(user.getId()).size() >= 3) {
            throw new TooManyConfirmationRequestsException();
        }

        if (user.isEnabled()) {
            throw new UserAlreadyEnabledException();
        }

        user.setEnabled(true);

        var authToken = UUID.randomUUID().toString();
        var tokenEntity = new AuthenticationTokenEntity(authToken, user);
        authenticationTokenRepository.save(tokenEntity);
        return new AuthenticationTokenResponseDto(authToken);
    }
}
