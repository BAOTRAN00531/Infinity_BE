package com.example.infinityweb_be.config;

import com.example.infinityweb_be.security.CustomOAuth2FailureHandler;
import com.example.infinityweb_be.security.CustomOAuth2SuccessHandler;
import com.example.infinityweb_be.service.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "https://infinitycat.site",
            "https://www.infinitycat.site"
    );

    // ✅ thêm đầy đủ endpoint public cho AI suggest + dictionary + TTS
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/", "/auth/**", "/oauth2/**", "/uploads/**",
            "/api/lexicon/test", "/api/lexicon/test-data", "/api/lexicon/phrases",
            "/api/lexicon/units",
            "/api/ai/lexicon/suggest",
            "/api/tts/**",
            "/api/ai/speaking/assess",
            "/api/ai/speaking/assess-json",
            "/api/ai/speaking/ai-assess",
            "/api/ai/speaking/test-assess",
            "/api/ai/speaking/upload-assess",
            "/api/ai/speaking/upload-assess-with-transcript",
            "/api/dictionary/**",
            "/api/tooltip/**",
            "/api/momo/**", "/api/vnpay/**", "/api/sepay/**",
            "/client/api/course/**",
            "/api/users/email/**",
            "/client/api/lexicon/gloss/**",
            "/api/student/dashboard/public", "/api/student/course/public/**",
            "/api/student/quiz/lesson/**",
            // ✅ Add lessons endpoints for development
            "/api/lessons/**",
            // ✅ Add courses endpoints for development
            "/api/courses/**",
            // ✅ Add languages endpoints for development
            "/api/languages/**",
            // ✅ Add modules endpoints for development
            "/api/modules/**",
            // ✅ Add uploads endpoints for development
            "/api/uploads/**"
    );

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthEntryPoint authEntryPoint;
    private final JwtDecoder jwtDecoder;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // ✅ cho phép preflight
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(PUBLIC_ENDPOINTS.toArray(new String[0])).permitAll()
                        .requestMatchers("/api/student/**").hasRole("STUDENT")
                        // Chỉ yêu cầu ADMIN cho các endpoint quản trị, KHÔNG áp cho AI/Dictionary/TTS
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/client/api/user/progress/**").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler(customOAuth2FailureHandler)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder))
                        .authenticationEntryPoint(authEntryPoint)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
                        .permitAll()
                );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(ALLOWED_ORIGINS);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("role");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
