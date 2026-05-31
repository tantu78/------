package com.campus.ball.repository;

import com.campus.ball.entity.ActivityRecommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRecommendRepository extends JpaRepository<ActivityRecommend, Long> {
    List<ActivityRecommend> findByReceiverIdAndIsRead(Long receiverId, Boolean isRead);
    List<ActivityRecommend> findByReceiverId(Long receiverId);
    int countByReceiverIdAndIsRead(Long receiverId, Boolean isRead);
}
