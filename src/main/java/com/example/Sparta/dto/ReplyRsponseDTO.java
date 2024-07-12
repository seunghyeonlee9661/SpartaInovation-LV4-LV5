package com.example.Sparta.dto;
import com.example.Sparta.entity.Comment;
import com.example.Sparta.entity.Lecture;
import com.example.Sparta.entity.Reply;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

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

