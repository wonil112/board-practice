package org.springboot.question.dto;

import lombok.*;
import org.springboot.question.entity.Question;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class QuestionDto {

    @Getter
    @AllArgsConstructor
    public static class Post {

        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @NotBlank(message = "내용은 공백이 아니어야 합니다.")
        private String content;

        private Question.QuestionSecret questionSecret;

        @Positive
        private long memberId;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private long questionId;

        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @NotBlank(message = "내용은 공백이 아니어야 합니다.")
        private String content;

        @Positive
        private long memberId;

        private Question.QuestionStatus questionStatus;

        public void setQuestionId(long questionId) {
            this.questionId = questionId;
        }
    }

    @Builder
    @Getter
    @Setter
    public static class Response {
        private Long questionId;
        private String title;
        private String content;
        private Question.QuestionStatus questionStatus;
        private long memberId;
        private String commentContent;
        private Question.QuestionSecret questionSecret;

        public String getQuestionStatus() {
            return questionStatus.getStepDescription();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Delete {
        private long memberId;
    }

}

