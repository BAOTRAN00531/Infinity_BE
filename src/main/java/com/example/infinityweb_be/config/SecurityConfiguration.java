package com.example.infinityweb_be.config;

import com.example.infinityweb_be.config.CustomAuthEntryPoint;

import com.example.infinityweb_be.security.CustomOAuth2FailureHandler;
import com.example.infinityweb_be.security.CustomOAuth2SuccessHandler;
import com.example.infinityweb_be.service.JwtAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Slf4j
public class SecurityConfiguration {
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthEntryPoint authEntryPoint;
    private final JwtDecoder jwtDecoder; // Tiêm JwtDecoder từ JwtConfig

    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    public SecurityConfiguration(JwtAuthFilter jwtAuthFilter, @Lazy CustomAuthEntryPoint authEntryPoint, @Autowired JwtDecoder jwtDecoder, CustomOAuth2SuccessHandler customOAuth2SuccessHandler, CustomOAuth2FailureHandler customOAuth2FailureHandler) {

        this.jwtAuthFilter = jwtAuthFilter;
        this.authEntryPoint = authEntryPoint;
        this.jwtDecoder = jwtDecoder;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.customOAuth2FailureHandler = customOAuth2FailureHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(corsFilter(), LogoutFilter.class)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/", "/auth/**", "/oauth2/**", "/uploads/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Test endpoints
                        .requestMatchers("/api/lexicon/test", "/api/lexicon/test-data").permitAll()


                        // 💡 Các endpoint này phải nằm TRƯỚC .requestMatchers("/api/**")
                        .requestMatchers("/api/momo/**").permitAll()
                        .requestMatchers("/api/vnpay/**").permitAll()
                        .requestMatchers("/client/api/course/**").permitAll()
                        .requestMatchers("/api/users/email/**").permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated() // BẮT BUỘC để sau cùng
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
                .formLogin(form -> form.disable());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("✅ Security filter chain configured.");
        return http.build();
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://web-infinityfe.vercel.app",
                "https://infinitycat.site"
        ));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}