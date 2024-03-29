package ru.itpark.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.itpark.filter.token.DefaultTokenAuthenticationFilter;

@Configuration
@EnableScheduling
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DefaultSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final DefaultTokenAuthenticationFilter tokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                .addFilterAfter(tokenFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/authentication").anonymous()
                .antMatchers(HttpMethod.POST, "/api/registration").anonymous()
                .antMatchers(HttpMethod.GET, "/api/registration/confirmation").anonymous()
                .antMatchers(HttpMethod.POST, "/api/recovery", "/api/recovery/confirmation*").anonymous()
                .antMatchers(HttpMethod.GET, "/*").anonymous()
                .antMatchers("/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/**").permitAll()

                .antMatchers("/api/iwschat").authenticated()
                .antMatchers("/api/users").authenticated()
                .antMatchers("/api/rooms").authenticated()
                .antMatchers("/api/profile").authenticated()
                .antMatchers("/api/**").hasRole("USER")
                .anyRequest().authenticated()
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public LocalValidatorFactoryBean validatorFactoryBean(MessageSource messageSource) {
        var bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}
