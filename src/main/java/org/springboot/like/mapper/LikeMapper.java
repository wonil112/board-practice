package org.springboot.like.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springboot.like.dto.LikeDto;
import org.springboot.like.entity.Like;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "questionId", target = "question.questionId")
    Like likePostToLike(LikeDto.Push requestBody);
}
