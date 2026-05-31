package com.campus.ball.controller;

import com.campus.ball.auth.AuthContext;
import com.campus.ball.common.Result;
import com.campus.ball.entity.ActivityEvaluation;
import com.campus.ball.service.EvaluationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation")
@Api(tags = "评价接口")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PostMapping("/submit")
    @ApiOperation("提交评价")
    public Result<?> submit(@RequestParam Long activityId, @RequestParam Long targetUserId,
                            @RequestParam Integer skillScore, @RequestParam Integer organizingScore,
                            @RequestParam(required = false) String comment) {
        Long evaluatorId = AuthContext.getUser().getId();
        return evaluationService.evaluate(evaluatorId, activityId, targetUserId, skillScore, organizingScore, comment);
    }

    @GetMapping("/activity/{activityId}")
    @ApiOperation("获取活动所有评价")
    public Result<List<ActivityEvaluation>> getActivityEvaluations(@PathVariable Long activityId) {
        return evaluationService.getActivityEvaluations(activityId);
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("获取用户收到的评价")
    public Result<List<ActivityEvaluation>> getUserEvaluations(@PathVariable Long userId) {
        return evaluationService.getUserEvaluations(userId);
    }

    @GetMapping("/user-score/{userId}")
    @ApiOperation("获取用户评分汇总")
    public Result<?> getUserScore(@PathVariable Long userId) {
        return evaluationService.getUserScoreSummary(userId);
    }
}
