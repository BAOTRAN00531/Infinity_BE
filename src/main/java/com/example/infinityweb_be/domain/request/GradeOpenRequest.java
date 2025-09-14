package com.example.infinityweb_be.domain.request;

import lombok.Data;

@Data
public class GradeOpenRequest {
    String text;
    String target;
    String lang;
}