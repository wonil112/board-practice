package org.springboot.like.service;

import org.springboot.exception.BusinessLogicException;
import org.springboot.exception.ExceptionCode;
import org.springboot.like.entity.Like;
import org.springboot.like.repository.LikeRepository;
import org.springboot.member.entity.Member;
import org.springboot.member.service.MemberService;
import org.springboot.question.entity.Question;
import org.springboot.question.repository.QuestionRepository;
import org.springboot.question.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final MemberService memberService;

    public LikeService(LikeRepository likeRepository, QuestionRepository questionRepository, QuestionService questionService, MemberService memberService) {
        this.likeRepository = likeRepository;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.memberService = memberService;
    }

    @Transactional
    public Like pushLike(Like like) {

        Question findQuestion = questionService.findQuestion(like.getQuestion().getQuestionId());
        Member findMember = memberService.findMember(like.getMember().getMemberId());


        Optional<Like> optionalLike = likeRepository.findByQuestionAndMember(findQuestion, findMember);


        Like resultLike;
        if(optionalLike.isPresent()){
            resultLike = optionalLike.get();
            if(resultLike.getLikeStatus().equals(Like.LikeStatus.LIKE_ON)){
                resultLike.setLikeStatus(Like.LikeStatus.LIKE_OFF);
                Question question = questionService.findQuestion(resultLike.getQuestion().getQuestionId());
                question.setLikeCount(question.getLikeCount() - 1);
            }else{
                resultLike.setLikeStatus(Like.LikeStatus.LIKE_ON);
                Question question = questionService.findQuestion(resultLike.getQuestion().getQuestionId());
                question.setLikeCount(question.getLikeCount() + 1);
            }
        }else{
            resultLike = new Like();
            resultLike.setMember(findMember);
            resultLike.setQuestion(findQuestion);
            Question question = questionService.findQuestion(resultLike.getQuestion().getQuestionId());
            question.setLikeCount(question.getLikeCount() + 1);
        }



        return likeRepository.save(resultLike);
    }
}

