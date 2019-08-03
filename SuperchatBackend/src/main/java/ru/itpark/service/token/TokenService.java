package ru.itpark.service.token;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface TokenService extends AuthenticationManager {

    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
