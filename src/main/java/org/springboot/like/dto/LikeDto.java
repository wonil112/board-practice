package org.springboot.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;

public class LikeDto {

    @Getter
    @AllArgsConstructor
    public static class Push {

        @Column(nullable = false)
        private long memberId;

        @Column(nullable = false)
        private long questionId;

        @Column(nullable = false)
        private String likeStatus;

    }
}
