package com.example.Sparta.dto;
import com.example.Sparta.entity.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentResponseDTO {
    private int user_id;
    private String userEmail;
    private String text;

    public CommentResponseDTO(Comment comment){
        this.user_id = comment.getUser().getId();
        this.userEmail = comment.getUser().getEmail();
        this.text = comment.getText();
    }
}
