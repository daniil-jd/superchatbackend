package ru.itpark.filter.token;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itpark.entrypoint.TokenAuthenticationEntryPoint;
import ru.itpark.service.token.TokenService;
import ru.itpark.token.DefaultAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DefaultTokenAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final TokenAuthenticationEntryPoint entryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = request.getHeader("X-Chat-Token");

        if (token == null && request.getServletPath().equals("/api/iwschat")) {
            token = request.getParameter("token");
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            var authRequest = new DefaultAuthenticationToken(token, null);
            var authResult = tokenService.authenticate(authRequest);

            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
