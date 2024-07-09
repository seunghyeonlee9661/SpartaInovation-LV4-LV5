package com.example.Sparta.dto;
import com.example.Sparta.entity.Comment;
import com.example.Sparta.entity.Lecture;
import com.example.Sparta.entity.Reply;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReplyRsponseDTO {
    private int user_id;
    private String userEmail;
    private String text;

    public ReplyRsponseDTO(Reply reply){
        this.user_id = reply.getUser().getId();
        this.userEmail = reply.getUser().getEmail();
        this.text = reply.getText();
    }
}
