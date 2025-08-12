package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.Comment;
import com.TripRider.TripRider.domain.Post;
import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.repository.CommentRepository;
import com.TripRider.TripRider.repository.PostRepository;
import com.TripRider.TripRider.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void addComment(Long postId, String content, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }


    public List<Comment> getCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        return commentRepository.findByPostOrderByCreatedAtAsc(post);
    }

    @Transactional
    public void deleteComment(Long commentId, User requester) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(requester.getId())) {
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }
}