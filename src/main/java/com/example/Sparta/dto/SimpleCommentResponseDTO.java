package com.example.Sparta.dto;
import com.example.Sparta.entity.Comment;
import com.example.Sparta.entity.Lecture;
import com.example.Sparta.enums.LectureCategory;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleCommentResponseDTO {
    private int id;
    private int user_id;
    private String userEmail;
    private String text;
    private List<SimpleReplyRsponseDTO> replies;

    public SimpleCommentResponseDTO(Comment comment){
        this.id = comment.getId();
        this.user_id = comment.getUser().getId();
        this.userEmail = comment.getUser().getEmail();
        this.text = comment.getText();
        this.replies = comment.getReplies().stream().map(SimpleReplyRsponseDTO::new).collect(Collectors.toList());
    }
}
