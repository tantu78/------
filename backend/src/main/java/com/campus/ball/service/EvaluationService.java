package com.campus.ball.service;

import com.campus.ball.common.Result;
import com.campus.ball.entity.Activity;
import com.campus.ball.entity.ActivityEvaluation;
import com.campus.ball.entity.User;
import com.campus.ball.repository.ActivityEvaluationRepository;
import com.campus.ball.repository.ActivityRepository;
import com.campus.ball.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationService {

    @Autowired
    private ActivityEvaluationRepository evaluationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    public Result<?> evaluate(Long evaluatorId, Long activityId, Long targetUserId, Integer skillScore, Integer organizingScore, String comment) {
        if (evaluatorId.equals(targetUserId)) {
            return Result.error("不能评价自己");
        }

        if (skillScore < 1 || skillScore > 5 || organizingScore < 1 || organizingScore > 5) {
            return Result.error("评分需在1-5之间");
        }

        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) {
            return Result.error("活动不存在");
        }

        if (!evaluationRepository.findByEvaluatorIdAndActivityIdAndTargetUserId(evaluatorId, activityId, targetUserId).isEmpty()) {
            return Result.error("已经评价过该用户了");
        }

        ActivityEvaluation evaluation = new ActivityEvaluation();
        evaluation.setActivityId(activityId);
        evaluation.setEvaluatorId(evaluatorId);
        evaluation.setTargetUserId(targetUserId);
        evaluation.setSkillScore(skillScore);
        evaluation.setOrganizingScore(organizingScore);
        evaluation.setComment(comment);
        evaluationRepository.save(evaluation);

        return Result.success("评价成功");
    }

    public Result<List<ActivityEvaluation>> getActivityEvaluations(Long activityId) {
        List<ActivityEvaluation> evaluations = evaluationRepository.findByActivityIdOrderByCreateTimeDesc(activityId);

        for (ActivityEvaluation e : evaluations) {
            userRepository.findById(e.getEvaluatorId()).ifPresent(u -> e.setEvaluatorName(u.getUsername()));
            userRepository.findById(e.getTargetUserId()).ifPresent(u -> e.setTargetUserName(u.getUsername()));
        }

        return Result.success(evaluations);
    }

    public Result<List<ActivityEvaluation>> getUserEvaluations(Long userId) {
        List<ActivityEvaluation> evaluations = evaluationRepository.findByTargetUserIdOrderByCreateTimeDesc(userId);

        for (ActivityEvaluation e : evaluations) {
            userRepository.findById(e.getEvaluatorId()).ifPresent(u -> e.setEvaluatorName(u.getUsername()));
        }

        return Result.success(evaluations);
    }

    public Result<?> getUserScoreSummary(Long userId) {
        Double avgSkill = evaluationRepository.avgSkillScoreByTargetUserId(userId);
        Double avgOrganizing = evaluationRepository.avgOrganizingScoreByTargetUserId(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("avgSkillScore", avgSkill != null ? Math.round(avgSkill * 10) / 10.0 : 0);
        result.put("avgOrganizingScore", avgOrganizing != null ? Math.round(avgOrganizing * 10) / 10.0 : 0);
        result.put("totalEvaluations", evaluationRepository.findByTargetUserIdOrderByCreateTimeDesc(userId).size());

        return Result.success(result);
    }
}
