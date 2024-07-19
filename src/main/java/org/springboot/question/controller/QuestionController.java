package org.springboot.question.controller;

import lombok.extern.slf4j.Slf4j;
import org.springboot.dto.MultiResponseDto;
import org.springboot.dto.SingleResponseDto;
import org.springboot.exception.BusinessLogicException;
import org.springboot.exception.ExceptionCode;
import org.springboot.member.entity.Member;
import org.springboot.question.dto.QuestionDto;
import org.springboot.question.entity.Question;
import org.springboot.question.mapper.QuestionMapper;
import org.springboot.question.repository.QuestionRepository;
import org.springboot.question.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springboot.utils.UriCreator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/questions")
@Validated
@Slf4j
public class QuestionController {

    private final static String QUESTION_DEFAULT_URL = "/v1/questions";
    private final QuestionService questionService;
    private final QuestionMapper mapper;
    private final QuestionRepository questionRepository;

    public QuestionController(QuestionService questionService, QuestionMapper mapper, QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.mapper = mapper;
        this.questionRepository = questionRepository;
    }

    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody QuestionDto.Post requestBody) {
        Question question = mapper.questionPostToQuestion(requestBody);
        Question createdQuestion = questionService.createQuestion(question);
        URI location = UriCreator.createUri(QUESTION_DEFAULT_URL, createdQuestion.getQuestionId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                        @Valid @RequestBody QuestionDto.Patch requestBody) {
        requestBody.setQuestionId(questionId);

        Question question = questionService.updateQuestion(mapper.questionPatchToQuestion(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.questionToQuestionResponse(question)), HttpStatus.OK);
    }

    @GetMapping("/{question-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive long questionId) {
        Question question = questionService.findQuestion(questionId);
        question.setViewCount(question.getViewCount() + 1);
        questionRepository.save(question);

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.questionToQuestionResponse(question))
                , HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getQuestions(@Positive @RequestParam int page,
                                       @Positive @RequestParam int size) {
        Page<Question> pageQuestions = questionService.findQuestions(page -1, size);
        List<Question> questions = pageQuestions.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.questionsToQuestionResponses(questions),
                        pageQuestions), HttpStatus.OK);
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity deleteQuestion(@PathVariable("question-id") long questionId,
                                         @Valid @RequestBody QuestionDto.Delete requestBody) {

        Question question = questionService.findQuestion(questionId);

        if (questionService.verifiedMemberId(question.getQuestionId(), requestBody.getMemberId())) {
            questionService.deleteQuestion(question.getQuestionId());
        } else {
            throw new BusinessLogicException(ExceptionCode.QUESTION_DELETE_NOT_PERMISSION);
        };
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
