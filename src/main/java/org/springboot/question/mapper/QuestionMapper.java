package org.springboot.question.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springboot.member.entity.Member;
import org.springboot.question.dto.QuestionDto;
import org.springboot.question.entity.Question;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {
    default Question questionPostToQuestion(QuestionDto.Post requestBody) {
        Question question = new Question();
        Member member = new Member();
        member.setMemberId(requestBody.getMemberId());

        question.setMember(member);
        question.setTitle(requestBody.getTitle());
        question.setContent(requestBody.getContent());
        if(requestBody.getQuestionSecret()!=null) {
            question.setQuestionSecret(requestBody.getQuestionSecret());
        }
        return question;
    };

    Question questionPatchToQuestion(QuestionDto.Patch requestBody);

    @Mapping(source = "comment.commentContent",target = "commentContent")
    @Mapping(source = "member.memberId",target = "memberId")
    QuestionDto.Response questionToQuestionResponse(Question question);

//    default Question questionDeleteToQuestion(QuestionDto.Delete requestBody) {
//        Question question = new Question();
//        Member member = new Member();
//        member.
//    }

    Question questionResponseToQuestion(QuestionDto.Response requestBody);

    List<QuestionDto.Response> questionsToQuestionResponses(List<Question> questions);


}
