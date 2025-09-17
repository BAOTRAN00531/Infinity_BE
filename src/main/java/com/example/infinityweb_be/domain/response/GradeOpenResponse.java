package com.example.infinityweb_be.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GradeOpenResponse {
    double score;
    String feedback;
}