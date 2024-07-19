package org.springboot.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springboot.comment.dto.CommentDto;
import org.springboot.comment.entity.Comment;
import org.springboot.question.dto.QuestionDto;
import org.springboot.question.entity.Question;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "questionId",target = "question.questionId")
    Comment commentPostToComment(CommentDto.Post requestBody);
    Comment commentPatchToComment(CommentDto.Patch requestBody);
    CommentDto.Response commentToCommentResponse(Comment comment);
}
