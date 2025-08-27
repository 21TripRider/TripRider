package com.TripRider.TripRider.repository;

import com.TripRider.TripRider.domain.Comment;
import com.TripRider.TripRider.domain.CommentLike;
import com.TripRider.TripRider.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByCommentAndUser(Comment comment, User user);
    long countByComment(Comment comment);
    void deleteByCommentAndUser(Comment comment, User user);
}
