package org.springboot.comment.service;

import org.springboot.comment.entity.Comment;
import org.springboot.comment.repository.CommentRepository;
import org.springboot.exception.BusinessLogicException;
import org.springboot.exception.ExceptionCode;
import org.springboot.helper.event.MemberRegistrationApplicationEvent;
import org.springboot.question.dto.QuestionDto;
import org.springboot.question.entity.Question;
import org.springboot.question.repository.QuestionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
//    private final ApplicationEventPublisher publisher;
    private final QuestionRepository questionRepository;

    public CommentService(CommentRepository commentRepository,QuestionRepository questionRepository) {
        this.commentRepository = commentRepository;
//        this.publisher = publisher;
        this.questionRepository = questionRepository;
    }

    public Comment createComment(Comment comment) {
//        Question findQuestion = commentService.findQuestion(requestBody.getQuestionId());
//        findQuestion.setQuestionStatus(Question.QuestionStatus.QUESTION_ANSWERED);
//
//        questionRepository.save(findQuestion);
//
//        QuestionDto.Response postCommentContent = questionMapper.questionToQuestionResponse(findQuestion);
//        postCommentContent.setCommentContent(requestBody.getCommentContent());
//        Question entityPostCommentContent = questionMapper.questionResponseToQuestion(postCommentContent);
//
//        questionRepository.save(entityPostCommentContent);
        Optional<Question> optionalQuestion  = questionRepository.findById(comment.getQuestion().getQuestionId());
        Question findQuestion = optionalQuestion.orElseThrow(()-> new BusinessLogicException(ExceptionCode.QUESTION_NOT_FOUND));
        findQuestion.setQuestionStatus(Question.QuestionStatus.QUESTION_ANSWERED);

        return commentRepository.save(comment);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Comment updateComment(Comment comment) {
        Comment findComment = findVerifiedComment(comment.getCommentId());

        Optional.ofNullable(comment.getCommentContent())
                .ifPresent(content -> findComment.setCommentContent(content));

        return commentRepository.save(findComment);
    }

    public void deleteComment(long commentId) {
        Comment findComment = findVerifiedComment(commentId);

        commentRepository.delete(findComment);
    }

    @Transactional(readOnly = true)
    public Comment findVerifiedComment(long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment fineComment = optionalComment.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.COMMENT_EXISTS));
        return fineComment;
    }

    public Question findQuestion(long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question findQuestion = optionalQuestion.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findQuestion;
    }

}
