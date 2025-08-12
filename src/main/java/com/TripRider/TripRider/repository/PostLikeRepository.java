package com.TripRider.TripRider.repository;

import com.TripRider.TripRider.domain.Post;
import com.TripRider.TripRider.domain.PostLike;
import com.TripRider.TripRider.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndUser(Post post, User user);
    long countByPost(Post post);
    void deleteByPostAndUser(Post post, User user);
}
