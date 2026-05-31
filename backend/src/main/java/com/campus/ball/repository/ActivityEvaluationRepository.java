package com.campus.ball.repository;

import com.campus.ball.entity.ActivityEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityEvaluationRepository extends JpaRepository<ActivityEvaluation, Long> {
    List<ActivityEvaluation> findByActivityIdOrderByCreateTimeDesc(Long activityId);
    List<ActivityEvaluation> findByTargetUserIdOrderByCreateTimeDesc(Long targetUserId);
    List<ActivityEvaluation> findByEvaluatorIdAndActivityId(Long evaluatorId, Long activityId);
    List<ActivityEvaluation> findByEvaluatorIdAndActivityIdAndTargetUserId(Long evaluatorId, Long activityId, Long targetUserId);
    
    @Query("SELECT AVG(e.skillScore) FROM ActivityEvaluation e WHERE e.targetUserId = ?1")
    Double avgSkillScoreByTargetUserId(Long targetUserId);
    
    @Query("SELECT AVG(e.organizingScore) FROM ActivityEvaluation e WHERE e.targetUserId = ?1")
    Double avgOrganizingScoreByTargetUserId(Long targetUserId);
}
