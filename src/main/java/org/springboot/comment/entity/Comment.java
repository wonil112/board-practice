package org.springboot.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springboot.audit.Auditable;
import org.springboot.question.entity.Question;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Setter
@Entity
public class Comment extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;
}
