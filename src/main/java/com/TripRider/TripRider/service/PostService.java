package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.Post;
import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.repository.PostRepository;
import com.TripRider.TripRider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 저장
    public void create(String content, String imageUrl, String location, String hashtags, User user) {
        Post post = Post.builder()
                .user(user)
                .content(content)
                .imageUrl(imageUrl)
                .location(location)
                .hashtags(hashtags)
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }


    // 게시글 목록
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    // 게시글 상세 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }
}
