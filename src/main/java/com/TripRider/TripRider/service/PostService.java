package com.TripRider.TripRider.service;

import com.TripRider.TripRider.domain.Post;
import com.TripRider.TripRider.domain.PostLike;
import com.TripRider.TripRider.domain.User;
import com.TripRider.TripRider.repository.CommentRepository;
import com.TripRider.TripRider.repository.PostLikeRepository;
import com.TripRider.TripRider.repository.PostRepository;
import com.TripRider.TripRider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

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

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, User requester) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 작성자 체크
        if (!post.getUser().getId().equals(requester.getId())) {
            throw new SecurityException("게시글 삭제 권한이 없습니다.");
        }

        // 댓글 먼저 삭제(연관관계 컬렉션이 없으므로 레포 메서드 사용)
        commentRepository.deleteByPost(post);

        // 게시글 삭제
        postRepository.delete(post);
    }

    // 게시글 좋아요 로직
    @Transactional
    public int likePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (postLikeRepository.existsByPostAndUser(post, user)) {
            // 이미 좋아요 한 경우 → 현재 카운트 반환(멱등)
            return post.getLikeCount();
        }

        try {
            PostLike like = PostLike.builder()
                    .post(post)
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build();
            postLikeRepository.save(like);
            post.increaseLikeCount();              // 캐시 증가
            postRepository.save(post);
        } catch (DataIntegrityViolationException e) {
            // 유니크 충돌 대비(동시성) → 무시하고 실제 카운트 재계산
        }

        // 소스오브트루스 보정(선택): 동시성 환경에서 안전
        int count = (int) postLikeRepository.countByPost(post);
        post.setLikeCount(count);
        return count;
    }

    @Transactional
    public int unlikePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!postLikeRepository.existsByPostAndUser(post, user)) {
            // 이미 좋아요 안한 상태 → 멱등
            return post.getLikeCount();
        }

        postLikeRepository.deleteByPostAndUser(post, user);
        post.decreaseLikeCount();                 // 캐시 감소
        postRepository.save(post);

        int count = (int) postLikeRepository.countByPost(post);
        post.setLikeCount(count);
        return count;
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return (int) postLikeRepository.countByPost(post);
    }
}