package com.example.Sparta.dto;
import com.example.Sparta.entity.Reply;
import lombok.Getter;

@Getter
public class SimpleReplyRsponseDTO {
    private int id;
    private int user_id;
    private String userEmail;
    private String text;

    public SimpleReplyRsponseDTO(Reply reply){
        this.id = reply.getId();
        this.user_id = reply.getUser().getId();
        this.userEmail = reply.getUser().getEmail();
        this.text = reply.getText();
    }
}
