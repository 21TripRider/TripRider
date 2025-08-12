package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.domain.*;
import com.TripRider.TripRider.dto.*;
import com.TripRider.TripRider.repository.UserRepository;
import com.TripRider.TripRider.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserRepository userRepository;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostResponse> result = posts.stream()
                .map(p -> PostResponse.builder()
                        .id(p.getId())
                        .content(p.getContent())
                        .imageUrl(p.getImageUrl())
                        .location(p.getLocation())
                        .hashtags(p.getHashtags())
                        .writer(p.getUser().getNickname())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // 게시글 등록
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest request,
                                        @AuthenticationPrincipal User user) {
        postService.create(request.getContent(),
                request.getImageUrl(), request.getLocation(), request.getHashtags(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body("게시글 작성 완료");
    }

    // 게시글 상세
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        PostResponse res = PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .location(post.getLocation())
                .hashtags(post.getHashtags())
                .writer(post.getUser().getNickname())
                .build();
        return ResponseEntity.ok(res);
    }

    // 댓글 작성
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestBody Comment comment,
                                        @AuthenticationPrincipal User user) {
        commentService.addComment(id, comment.getContent(), user);
        return ResponseEntity.ok("댓글 등록 완료");
    }

    // 댓글 목록
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<String>> getComments(@PathVariable Long id) {
        List<Comment> comments = commentService.getCommentsForPost(id);
        List<String> contents = comments.stream()
                .map(Comment::getContent)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contents);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                           @AuthenticationPrincipal User user) {
        postService.deletePost(id, user);
        return ResponseEntity.noContent().build(); // 204
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              @AuthenticationPrincipal User user) {
        // (선택) postId 검증하고 싶다면 comment 조회 후 comment.getPost().getId() 비교
        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build(); // 204
    }
}