package org.springboot.like.repository;

import org.springboot.like.entity.Like;
import org.springboot.member.entity.Member;
import org.springboot.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByQuestionAndMember (Question question, Member member);
}
