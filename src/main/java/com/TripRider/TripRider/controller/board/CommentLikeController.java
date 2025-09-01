package com.TripRider.TripRider.controller.board;

import com.TripRider.TripRider.domain.user.User;
import com.TripRider.TripRider.service.board.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments/{commentId}/likes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    /** 댓글 좋아요 */
    @PostMapping
    public long like(@PathVariable Long commentId,
                     @AuthenticationPrincipal User user) {
        return commentLikeService.likeComment(commentId, user);
    }

    /** 댓글 좋아요 취소 */
    @DeleteMapping
    public long unlike(@PathVariable Long commentId,
                       @AuthenticationPrincipal User user) {
        return commentLikeService.unlikeComment(commentId, user);
    }

    /** 댓글 좋아요 개수 조회 */
    @GetMapping("/count")
    public long count(@PathVariable Long commentId) {
        return commentLikeService.countLikes(commentId);
    }

    /** 내가 좋아요 눌렀는지 여부 확인 */
    @GetMapping("/me")
    public boolean likedByMe(@PathVariable Long commentId,
                             @AuthenticationPrincipal User user) {
        return commentLikeService.likedByMe(commentId, user);
    }
}
