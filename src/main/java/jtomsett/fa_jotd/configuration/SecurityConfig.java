package jtomsett.fa_jotd.configuration;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2ResourceServerProperties properties;

    @Bean
    BearerTokenResolver bearerTokenResolver() {
        return request -> {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Optional<Cookie> cookie = Arrays.stream(cookies)
                        .filter(name -> name.getName().equals("app.at"))
                        .findFirst();
                if (cookie.isPresent()) {
                    return cookie.get().getValue();
                }
            }

            DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();
            return defaultBearerTokenResolver.resolve(request);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> audiences = properties.getJwt().getAudiences();
        CustomJwtAuthenticationConverter converter = new CustomJwtAuthenticationConverter(audiences);

        return http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/joke")
                    .hasAnyAuthority("User","Admin","Creator")
                .requestMatchers("/joke/add")
                    .hasAnyAuthority("Admin","Creator")
                .requestMatchers("/joke/update","/joke/delete/")
                    .hasAnyAuthority("Admin")
                .requestMatchers("/swagger-ui/**","/v3/api-docs/**")
                        .permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(converter)))
                .build();

    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation(properties.getJwt().getIssuerUri())
                .build();
    }
}
