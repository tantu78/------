package com.campus.ball.repository;

import com.campus.ball.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByTargetUserId(Long targetUserId);
    List<Evaluation> findByActivityId(Long activityId);
    boolean existsByEvaluatorIdAndTargetUserIdAndActivityId(Long evaluatorId, Long targetUserId, Long activityId);
}
