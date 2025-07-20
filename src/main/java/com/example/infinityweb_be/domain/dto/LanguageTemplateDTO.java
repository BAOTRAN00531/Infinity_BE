package com.example.infinityweb_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageTemplateDTO {
    private String name;
    private String code;
    private String flag;

}
