package org.springboot.helper.event;

import lombok.Getter;
import org.springboot.comment.entity.Comment;
import org.springboot.member.entity.Member;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberRegistrationApplicationEvent extends ApplicationEvent {

    private Member member;

    public MemberRegistrationApplicationEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }

}
