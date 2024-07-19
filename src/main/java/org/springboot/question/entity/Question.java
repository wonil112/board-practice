package org.springboot.question.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.Builder;
import org.springboot.audit.Auditable;
import org.springboot.comment.entity.Comment;
import org.springboot.like.entity.Like;
import org.springboot.member.entity.Member;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Question extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTERED;

    @Enumerated(EnumType.STRING)
    private QuestionSecret questionSecret = QuestionSecret.QUESTION_PUBLIC;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToOne(mappedBy = "question")
    private Comment comment;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @Column(nullable = false)
    private Integer viewCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    public void setMember(Member member) {
        this.member = member;
        if(member.getQuestions().equals(this)) {
            member.setQuestion(this);
        }
    }

    public void setLike(Like like) {

        if(like.getQuestion() != this) {
            like.setQuestion(this);
        }
        likes.add(like);
    }

    public enum QuestionStatus {
        QUESTION_REGISTERED(1, "질문 등록 상태"),
        QUESTION_ANSWERED(2, "답변 완료 상태"),
        QUESTION_DELETED(3, "질문 삭제 상태"),
        QUESTION_DEACTIVED(4, "질문 비활성화 상태");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        QuestionStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }

    public enum QuestionSecret {
        QUESTION_PUBLIC("공개글"),
        QUESTION_SECRET("비밀글");

        @Getter
        private String stepDescription;

        QuestionSecret(String stepDescription) {
            this.stepDescription = stepDescription;
        }
    }



}
