package com.example.infinityweb_be.domain.map;

import com.example.infinityweb_be.domain.LearningModule;
import com.example.infinityweb_be.domain.dto.modules.LearningModuleDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LearningModuleMapper {
    LearningModuleDto toDto(LearningModule module);
}
