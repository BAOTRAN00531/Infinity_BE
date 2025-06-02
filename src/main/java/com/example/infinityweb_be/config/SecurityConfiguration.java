package com.example.infinityweb_be.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    //config SecurityFilter temporarily disabled
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthEntryPoint CEP) throws Exception {
        http
                .csrf(c->c.disable())
                .authorizeHttpRequests(
                        authz -> authz
                                //Setup if it's injected for token,then activity for flow chat wil be done .Else it isn't have so it alert warning about "401 Unauthorized"
                                .requestMatchers("/","/login", "/indexx","/secure","/refreshh","/public").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(CEP))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(CEP)
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))

                .formLogin(f ->f.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Config proxy variable for key of jwt
    @Value("${assigment_java6.jwt.base64-secret}")
    private String jwtKeyPath;
    // Config proxy variable for expiration of jwt
    @Value("${assigment_java6.jwt.access-token-validity-in-seconds}")
    private String jwtExpiryInSeconds;
    //Config func to encoder hash password
    @Bean
    public JwtEncoder getjwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getsecretKey()));
    }

    @Bean
    public JwtAuthenticationConverter JwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("user");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtAuthenticationConverter;
    }
    //    Config the function to decode a encoded variable
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getsecretKey()).macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }

    //    Because the key is in Base64,it must be decoded and saved in byte format
    private SecretKey getsecretKey(){
        byte[] keyBytes = Base64.from(jwtKeyPath).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
    };
}
