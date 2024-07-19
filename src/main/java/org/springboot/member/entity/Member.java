package org.springboot.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springboot.audit.Auditable;
import org.springboot.like.entity.Like;
import org.springboot.question.entity.Question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @OneToMany(mappedBy = "member")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 13, unique = true)
    private String phone;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    public void setQuestion(Question question) {

        if(question.getMember() != this) {
            question.setMember(this);
        }
        questions.add(question);
    }

    public void setLike(Like like) {

        if(like.getMember() != this) {
            like.setMember(this);
        }
        likes.add(like);
    }

    public enum MemberStatus {
        MEMBER_ACTIVE(1, "활동중"),
        MEMBER_SLEEP(2, "휴면 상태"),
        MEMBER_QUIT(3, "탈퇴 상태");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        MemberStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }

}
