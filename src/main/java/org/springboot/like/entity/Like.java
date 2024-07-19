package org.springboot.like.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springboot.audit.Auditable;
import org.springboot.member.entity.Member;
import org.springboot.question.entity.Question;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Likes")
public class Like extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    public void setMember(Member member) {
        this.member = member;
        if(member.getLikes().equals(this)) {
            member.setLike(this);
        }
    }

    public void setQuestion(Question question) {
        this.question = question;
        if(question.getLikes().equals(this)) {
            question.setLike(this);
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeStatus likeStatus = LikeStatus.LIKE_ON;

    public enum LikeStatus {
        LIKE_ON("좋아요 등록"),
        LIKE_OFF("좋아요 해제");

        @Getter
        private String statusDescription;

        LikeStatus(String statusDescription) {
            this.statusDescription = statusDescription;
        }


    }


}
