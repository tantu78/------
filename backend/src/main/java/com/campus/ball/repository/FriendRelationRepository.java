package com.campus.ball.repository;

import com.campus.ball.entity.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {
    List<FriendRelation> findByUserIdAndStatus(Long userId, String status);
    List<FriendRelation> findByFriendIdAndStatus(Long friendId, String status);
    Optional<FriendRelation> findByUserIdAndFriendId(Long userId, Long friendId);
    boolean existsByUserIdAndFriendIdAndStatus(Long userId, Long friendId, String status);
}
