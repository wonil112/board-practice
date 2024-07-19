package org.springboot.like.controller;

import lombok.extern.slf4j.Slf4j;
import org.springboot.like.dto.LikeDto;
import org.springboot.like.entity.Like;
import org.springboot.like.mapper.LikeMapper;
import org.springboot.like.service.LikeService;
import org.springboot.utils.UriCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/likes")
@Validated
@Slf4j
public class LikeController {
    private final static String LIKE_DEFAULT_URL = "/v1/likes";
    private final LikeService likeService;
    private final LikeMapper mapper;

    public LikeController(LikeService likeService, LikeMapper mapper) {
        this.likeService = likeService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity pushLike(@Valid @RequestBody LikeDto.Push requestBody) {
        Like like = mapper.likePostToLike(requestBody);
        Like createdLike = likeService.pushLike(like);

        URI location = UriCreator.createUri(LIKE_DEFAULT_URL, createdLike.getLikeId());

        return ResponseEntity.created(location).build();
    }
}
