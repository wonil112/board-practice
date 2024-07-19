package org.springboot.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

public class CommentDto {

    @Getter
    @AllArgsConstructor
    public static class Post {

        @Column(nullable = false)
        private String commentContent;

        @Column(nullable = false)
        private long questionId;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {

        @Column(nullable = false)
        private long commentId;

        @Column(nullable = false)
        private String commentContent;

        public void setCommentId(long commentId) {
            this.commentId = commentId;
        }
    }

    @Getter
    @AllArgsConstructor
    @Setter
    public static class Response {

        private long commentId;
        private String commentContent;
    }


}
