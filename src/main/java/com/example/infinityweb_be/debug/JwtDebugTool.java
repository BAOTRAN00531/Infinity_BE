//// Test class để debug JWT secret key
//package com.example.infinityweb_be.debug;
//
//import com.nimbusds.jose.util.Base64;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.spec.SecretKeySpec;
//
//@Component
//public class JwtDebugTool implements CommandLineRunner {
//
//    @Override
//    public void run(String... args) throws Exception {
//        String base64Secret = "dBwVO7xfkyo+fTi9nbIFvOu+wMXs2k8DXHyKaoV2+bYSENPXQwGokJ8exOJdL1zs/FuTuTzpkndfAEmyAcI1Bw==";
//
//        try {
//            System.out.println("=== JWT SECRET KEY DEBUG ===");
//            System.out.println("Base64 Secret: " + base64Secret);
//
//            // Test decode
//            byte[] keyBytes = Base64.from(base64Secret).decode();
//            System.out.println("Decoded length: " + keyBytes.length + " bytes");
//            System.out.println("Is valid length (>= 32): " + (keyBytes.length >= 32));
//
//            // Test creating SecretKeySpec
//            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
//            System.out.println("Secret key algorithm: " + secretKey.getAlgorithm());
//            System.out.println("Secret key format: " + secretKey.getFormat());
//
//            System.out.println("✅ JWT Secret Key is VALID");
//
//        } catch (Exception e) {
//            System.out.println("❌ JWT Secret Key ERROR: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}