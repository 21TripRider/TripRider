package com.TripRider.TripRider.controller;

import com.TripRider.TripRider.domain.Comment;
import com.TripRider.TripRider.domain.Post;
import com.TripRider.TripRider.service.CommentService;
import com.TripRider.TripRider.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    // 게시글 목록 페이지
    @GetMapping("/board")
    public String board(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "board";
    }

    // 게시글 작성 폼
    @GetMapping("/board/new")
    public String createForm() {
        return "boardForm";
    }

    // 게시글 저장 처리
    @PostMapping("/board")
    public String createPost(@RequestParam String content,
                             @RequestParam(required = false) String imageUrl,
                             @RequestParam(required = false) String location,
                             @RequestParam(required = false) String hashtags) {
        postService.create(content, imageUrl, location, hashtags);
        return "redirect:/board";
    }

    // 게시글 상세 + 댓글 목록
    @GetMapping("/board/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsForPost(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "boardDetail";
    }

    // 댓글 등록 처리
    @PostMapping("/board/{id}/comment")
    public String addComment(@PathVariable Long id, @RequestParam String content) {
        commentService.addComment(id, content);
        return "redirect:/board/" + id;
    }
}
