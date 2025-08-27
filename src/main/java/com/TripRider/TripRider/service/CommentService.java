package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.Comment;
import com.TripRider.TripRider.domain.Post;
import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.domain.CommentLike;
import com.TripRider.TripRider.dto.board.CommentResponse;
import com.TripRider.TripRider.repository.CommentLikeRepository;
import com.TripRider.TripRider.repository.CommentRepository;
import com.TripRider.TripRider.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    /** 댓글 작성 */
    public void addComment(Long postId, String content, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        commentRepository.save(comment);
    }

    /** 댓글 조회 (좋아요 개수 + 내가 좋아요 눌렀는지 여부 포함) */
    public List<CommentResponse> getCommentsForPost(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        return commentRepository.findByPostOrderByCreatedAtAsc(post).stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .user(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .likeCount((int) commentLikeRepository.countByComment(comment))
                        .likedByMe(currentUser != null &&
                                commentLikeRepository.existsByCommentAndUser(comment, currentUser))
                        .mine(currentUser != null &&
                                comment.getUser().getId().equals(currentUser.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    /** 댓글 삭제 */
    @Transactional
    public void deleteComment(Long commentId, User requester) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(requester.getId())) {
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }

    /** ✅ 댓글 좋아요 */
    @Transactional
    public int likeComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        if (!commentLikeRepository.existsByCommentAndUser(comment, user)) {
            CommentLike like = CommentLike.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            commentLikeRepository.save(like);
        }
        return (int) commentLikeRepository.countByComment(comment);
    }

    /** ✅ 댓글 좋아요 취소 */
    @Transactional
    public int unlikeComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        if (commentLikeRepository.existsByCommentAndUser(comment, user)) {
            commentLikeRepository.deleteByCommentAndUser(comment, user);
        }
        return (int) commentLikeRepository.countByComment(comment);
    }
}
