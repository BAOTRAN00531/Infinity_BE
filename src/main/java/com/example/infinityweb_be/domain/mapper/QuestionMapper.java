// QuestionMapper.java
package com.example.infinityweb_be.domain.mapper;

import com.example.infinityweb_be.domain.Question;
import com.example.infinityweb_be.domain.dto.question.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(source = "content",          target = "questionText")
//    @Mapping(source = "course.id",        target = "courseId")
//    @Mapping(source = "lesson.id",        target = "lessonId")
    @Mapping(source = "questionType.id",      target = "questionTypeId")
    @Mapping(source = "mediaUrl",             target = "media.mediaUrl")
    @Mapping(source = "audioUrl",             target = "media.audioUrl")
    @Mapping(source = "videoUrl",             target = "media.videoUrl")
    @Mapping(source = "level",                target = "difficulty")
    @Mapping(source = "score",                target = "points")
    @Mapping(source = "createdBy.id",         target = "createdBy")
    @Mapping(source = "createdAt",            target = "createdAt")
    @Mapping(source = "updatedBy.id",         target = "updatedBy")
    @Mapping(source = "updatedAt",            target = "updatedAt")
    @Mapping(source = "lesson.id",            target = "lessonId")
    @Mapping(source = "lesson.name",          target = "lessonName")
    @Mapping(source = "lesson.module.id",     target = "moduleId")
    @Mapping(source = "lesson.module.name",   target = "moduleName")
    QuestionResponseDto toResponseDto(Question question);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "questionText",     target = "content")
//    @Mapping(source = "courseId",         target = "course.id")
//    @Mapping(source = "lessonId",         target = "lesson.id")
    @Mapping(source = "questionTypeId",       target = "questionType.id")
    @Mapping(source = "media.mediaUrl",       target = "mediaUrl")
    @Mapping(source = "media.audioUrl",       target = "audioUrl")
    @Mapping(source = "media.videoUrl",       target = "videoUrl")
    @Mapping(source = "difficulty",           target = "level")
    @Mapping(source = "points",               target = "score")
    Question toEntity(QuestionCreateDto dto);
}
