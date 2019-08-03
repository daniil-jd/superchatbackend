package ru.itpark.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itpark.dto.AuthenticationTokenRequestDto;
import ru.itpark.dto.AuthenticationTokenResponseDto;
import ru.itpark.service.AuthenticationService;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping
    public AuthenticationTokenResponseDto authenticate(@RequestBody AuthenticationTokenRequestDto dto) {
        return service.authenticate(dto);
    }
}
