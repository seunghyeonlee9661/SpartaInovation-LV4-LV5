package com.example.Sparta.dto.response;
import com.example.Sparta.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

/* 대댓글 내용 반환 */
@Getter
public class ReplyRsponseDTO {
    private int id;
    private int user_id;
    private String user_email;
    private String text;
    private LocalDateTime regist;

    public ReplyRsponseDTO(Reply reply){
        this.id = reply.getId();
        this.user_id = reply.getUser().getId();
        this.user_email = reply.getUser().getEmail();
        this.text = reply.getText();
        this.regist = reply.getRegist();
    }
}

