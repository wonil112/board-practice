package org.springboot.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springboot.comment.dto.CommentDto;
import org.springboot.comment.entity.Comment;
import org.springboot.comment.mapper.CommentMapper;
import org.springboot.comment.service.CommentService;
import org.springboot.dto.SingleResponseDto;
import org.springboot.question.dto.QuestionDto;
import org.springboot.question.entity.Question;
import org.springboot.question.mapper.QuestionMapper;
import org.springboot.question.repository.QuestionRepository;
import org.springboot.question.service.QuestionService;
import org.springboot.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/v1/comments")
@Validated
@Slf4j
public class CommentController {
    private final static String COMMENT_DEFAULT_URL = "/v1/comments";
    private final CommentService commentService;
    private final CommentMapper mapper;

    public CommentController(CommentService commentService, CommentMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postComment(@Valid @RequestBody CommentDto.Post requestBody) {
        Comment comment = mapper.commentPostToComment(requestBody);
        Comment createdComment = commentService.createComment(comment);
        URI location = UriCreator.createUri(COMMENT_DEFAULT_URL, createdComment.getCommentId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@PathVariable("comment-id") @Positive long commentId,
                                           @Valid @RequestBody CommentDto.Patch requestBody){
        requestBody.setCommentId(commentId);

        Comment comment = commentService.updateComment(mapper.commentPatchToComment(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.commentToCommentResponse(comment)), HttpStatus.OK);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") @Positive long commentId){

        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
