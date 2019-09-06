package ru.itpark.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itpark.exception.AuthenticateTokenException;
import ru.itpark.repository.AuthenticationTokenRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultAuthenticationInFilterService implements TokenService {
    private final AuthenticationTokenRepository authenticationTokenRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = authentication.getPrincipal();

        if (token == null) {
            throw new AuthenticateTokenException("api.exception.authenticate.token.null.message");
        }
        var tokenEntity = authenticationTokenRepository
                .findById(token.toString())
                .orElseThrow(() -> new AuthenticateTokenException("api.exception.authenticate.token.invalid.message"));
        var userEntity = tokenEntity.getUser();

        return new UsernamePasswordAuthenticationToken(
                userEntity,
                userEntity.getPassword(),
                userEntity.getAuthorities()
        );
    }
}
