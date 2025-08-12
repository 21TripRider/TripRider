package com.TripRider.TripRider.repository;

import com.TripRider.TripRider.domain.Comment;
import com.TripRider.TripRider.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);

    void deleteByPost(Post post); //삭제 게시물에 대한 댓글 자동 삭제
}