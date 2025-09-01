package com.TripRider.TripRider.controller.board;

import com.TripRider.TripRider.domain.board.Post;
import com.TripRider.TripRider.domain.user.User;
import com.TripRider.TripRider.dto.board.PostRequest;
import com.TripRider.TripRider.dto.board.PostResponse;
import com.TripRider.TripRider.repository.board.CommentRepository;
import com.TripRider.TripRider.repository.board.PostLikeRepository;
import com.TripRider.TripRider.repository.user.UserRepository;
import com.TripRider.TripRider.service.board.CommentService;
import com.TripRider.TripRider.service.board.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.TripRider.TripRider.dto.board.CommentRequest;
import com.TripRider.TripRider.dto.board.CommentResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;
    private final CommentService commentService;

    //  게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@AuthenticationPrincipal User user) {
        List<PostResponse> posts = postService.getAllPosts(user); // ✅ User 전달
        return ResponseEntity.ok(posts);
    }

    //  게시글 등록
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest request,
                                        @AuthenticationPrincipal User user) {
        postService.create(
                request.getContent(),
                request.getImageUrl(),
                request.getLocation(),
                request.getHashtags(),
                user
        );
        return ResponseEntity.status(HttpStatus.CREATED).body("게시글 작성 완료");
    }

    //  게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id,
                                                @AuthenticationPrincipal User user) {
        PostResponse res = postService.getPostById(id, user); // ✅ User 전달
        return ResponseEntity.ok(res);
    }

    //  댓글 작성
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestBody CommentRequest req,
                                        @AuthenticationPrincipal User user) {
        commentService.addComment(id, req.getContent(), user);
        return ResponseEntity.ok("댓글 등록 완료");
    }

    //  댓글 목록
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id,
                                                             @AuthenticationPrincipal User me) {
        List<CommentResponse> comments = commentService.getCommentsForPost(id, me);
        return ResponseEntity.ok(comments);
    }

    //  댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              @AuthenticationPrincipal User user) {
        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }

    //  게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        postService.deletePost(id, user);
        return ResponseEntity.noContent().build();
    }

    //  게시글 좋아요 추가
    @PostMapping("/{id}/likes")
    public ResponseEntity<?> like(@PathVariable Long id,
                                  @AuthenticationPrincipal User user) {
        int count = postService.likePost(id, user);
        return ResponseEntity.ok(Map.of("likeCount", count, "liked", true));
    }

    //  게시글 좋아요 취소
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<?> unlike(@PathVariable Long id,
                                    @AuthenticationPrincipal User user) {
        int count = postService.unlikePost(id, user);
        return ResponseEntity.ok(Map.of("likeCount", count, "liked", false));
    }

    //  게시글 좋아요 개수 확인
    @GetMapping("/{id}/likes/count")
    public ResponseEntity<?> likeCount(@PathVariable Long id) {
        int count = postService.getLikeCount(id);
        return ResponseEntity.ok(Map.of("likeCount", count));
    }
}
