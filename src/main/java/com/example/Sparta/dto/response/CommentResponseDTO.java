package com.example.Sparta.dto.response;
import com.example.Sparta.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDTO {
    private int id;
    private int user_id;
    private String user_email;
    private String text;
    private List<ReplyRsponseDTO> replies;
    private LocalDateTime regist;

    public CommentResponseDTO(Comment comment){
        this.id = comment.getId();
        this.user_id = comment.getUser().getId();
        this.user_email = comment.getUser().getEmail();
        this.text = comment.getText();
        this.replies = comment.getReplies().stream().map(ReplyRsponseDTO::new).collect(Collectors.toList());
        this.regist = comment.getRegist();
    }
}
