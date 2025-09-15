//package com.example.infinityweb_be.common;
//
//import com.example.infinityweb_be.domain.dto.LexiconDTOs;
//import com.example.infinityweb_be.service.LexiconSuggestService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//class LexiconWarmup implements CommandLineRunner {
//
//    private final LexiconSuggestService svc;
//
//    @Override
//    public void run(String... args) {
//        // tiền tố a..z. Nếu muốn nhiều hơn, thêm "th","fl","wh",...
//        List<String> prefixes = "abcdefghijklmnopqrstuvwxyz"
//                .chars().mapToObj(c -> String.valueOf((char) c)).toList();
//
//        prefixes.forEach(p -> {
//            try {
//                LexiconDTOs.SuggestRequest req = new LexiconDTOs.SuggestRequest();
//                req.setPrefix(p);
//                req.setLang("en");
//                req.setLevel("beginner");
//                svc.suggest(req); // warm cache, bỏ kết quả
//            } catch (Exception ignore) {}
//        });
//    }
//}
//
