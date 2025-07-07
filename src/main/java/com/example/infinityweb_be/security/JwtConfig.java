package com.example.infinityweb_be.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.*;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.*;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public KeyPair keyPair() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("keys/private.pem"); // ✅ sửa đúng đường dẫn
        if (is == null) {
            throw new IllegalArgumentException("Không tìm thấy file private.pem trong thư mục resources/keys");
        }

        String pem = new String(is.readAllBytes())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(spec);

        RSAPrivateCrtKeySpec privateCrtKeySpec = kf.getKeySpec(privateKey, RSAPrivateCrtKeySpec.class);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                privateCrtKeySpec.getModulus(),
                privateCrtKeySpec.getPublicExponent()
        );
        PublicKey publicKey = kf.generatePublic(publicKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((java.security.interfaces.RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID("rsa-key-id")
                .build();

        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder.withPublicKey((java.security.interfaces.RSAPublicKey) keyPair.getPublic()).build();
    }
}
