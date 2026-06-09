package com.campus.ball.repository;

import com.campus.ball.entity.ActivityAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityAuditLogRepository extends JpaRepository<ActivityAuditLog, Long> {
    List<ActivityAuditLog> findByActivityIdOrderByCreateTimeDesc(Long activityId);
}
