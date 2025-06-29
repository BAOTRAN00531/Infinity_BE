package com.example.infinityweb_be.domain.dto.question;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaDto {
    private String mediaUrl;
    private String audioUrl;
    private String videoUrl;
}