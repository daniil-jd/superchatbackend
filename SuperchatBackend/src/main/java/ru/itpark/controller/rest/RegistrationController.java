package ru.itpark.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itpark.dto.AuthenticationTokenResponseDto;
import ru.itpark.dto.RegistrationRequestDto;
import ru.itpark.service.RegistrationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registration(@Valid @RequestBody RegistrationRequestDto dto) {
        service.register(dto);
    }

    @GetMapping("/confirmation")
    public AuthenticationTokenResponseDto confirmation(@RequestParam String token) {
        return service.confirm(token);
    }

}
