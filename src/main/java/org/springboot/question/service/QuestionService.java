package org.springboot.question.service;

import org.springboot.exception.BusinessLogicException;
import org.springboot.exception.ExceptionCode;
import org.springboot.like.entity.Like;
import org.springboot.question.dto.QuestionDto;
import org.springboot.question.entity.Question;
import org.springboot.question.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question createQuestion(Question question) {

        return questionRepository.save(question);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Question updateQuestion(Question question) {
        Question findQuestion = findVerifiedQuestion(question.getQuestionId());

        Optional.ofNullable(question.getTitle())
                .ifPresent(title -> findQuestion.setTitle(title));
        Optional.ofNullable(question.getContent())
                .ifPresent(content -> findQuestion.setContent(content));
        Optional.ofNullable(question.getQuestionStatus())
                .ifPresent(questionStatus -> findQuestion.setQuestionStatus(questionStatus));
        Optional.ofNullable(question.getQuestionSecret())
                .ifPresent(questionSecret -> findQuestion.setQuestionSecret(questionSecret));

        return questionRepository.save(findQuestion);
    }

    @Transactional(readOnly = true)
    public Question findQuestion(long questionId) {

        Question question = findVerifiedQuestion(questionId);
        if (question.getQuestionStatus().getStepNumber() == 3 ||
                question.getQuestionStatus().getStepNumber() == 4) {
           throw new BusinessLogicException(ExceptionCode.QUESTION_NOT_VIEWABLE);
        }

        return question;
    }

    public Page<Question> findQuestions(int page, int size) {
        return questionRepository.findAll(PageRequest.of(page,size,
                Sort.by("questionId").descending()));
    }

    public void deleteQuestion(long questionId) {
        Question findQuestion = findVerifiedQuestion(questionId);
        findQuestion.setQuestionStatus(Question.QuestionStatus.QUESTION_DELETED);
        questionRepository.save(findQuestion);
    }

    private Integer countLike(long questionId) {
        int count = 0;
        Question question = findVerifiedQuestion(questionId);

        for (Like like : question.getLikes()) {
            if (like.getLikeStatus().equals(Like.LikeStatus.LIKE_ON)) {
                count++;
            }
        }
        return count;
    }

    @Transactional(readOnly = true)
    private Question findVerifiedQuestion(long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question findQuestion =
                optionalQuestion.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.QUESTION_EXISTS));
        return findQuestion;
    }

    public boolean verifiedMemberId(long questionId, long memberId) {
        Question question = findQuestion(questionId);
        if (question.getMember().getMemberId() == memberId) {
            return true;
        } else {
            return false;
        }
    }
}
