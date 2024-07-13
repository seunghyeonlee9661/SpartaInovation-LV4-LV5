package com.example.Sparta.entity;

import com.example.Sparta.dto.request.ReplyUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name="sparta_reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, columnDefinition = "int")
    private int id;

    @ManyToOne
    @JoinColumn(name="comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @CreationTimestamp
    @Column(name="regist")
    private LocalDateTime regist;

    public Reply(Comment comment, User user, String text) {
        this.user = user;
        this.comment = comment;
        this.text = text;
    }

    public void update(ReplyUpdateRequestDTO replyUpdateRequestDTO) {
        this.text = replyUpdateRequestDTO.getText();
    }
}
