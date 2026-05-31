package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_evaluation")
@ApiModel(value = "活动评价实体")
public class ActivityEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @Column(name = "activity_id", nullable = false)
    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @Column(name = "evaluator_id", nullable = false)
    @ApiModelProperty(value = "评价者用户ID")
    private Long evaluatorId;

    @Column(name = "target_user_id", nullable = false)
    @ApiModelProperty(value = "被评价用户ID")
    private Long targetUserId;

    @Column(name = "skill_score", nullable = false)
    @ApiModelProperty(value = "球技评分 1-5")
    private Integer skillScore;

    @Column(name = "organizing_score", nullable = false)
    @ApiModelProperty(value = "组织能力评分 1-5")
    private Integer organizingScore;

    @ApiModelProperty(value = "评价内容")
    private String comment;

    @Column(name = "create_time")
    @ApiModelProperty(value = "评价时间")
    private LocalDateTime createTime;

    @Transient
    @ApiModelProperty(value = "评价者名称")
    private String evaluatorName;

    @Transient
    @ApiModelProperty(value = "被评价者名称")
    private String targetUserName;

    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getEvaluatorId() { return evaluatorId; }
    public void setEvaluatorId(Long evaluatorId) { this.evaluatorId = evaluatorId; }
    public Long getTargetUserId() { return targetUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }
    public Integer getSkillScore() { return skillScore; }
    public void setSkillScore(Integer skillScore) { this.skillScore = skillScore; }
    public Integer getOrganizingScore() { return organizingScore; }
    public void setOrganizingScore(Integer organizingScore) { this.organizingScore = organizingScore; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getEvaluatorName() { return evaluatorName; }
    public void setEvaluatorName(String evaluatorName) { this.evaluatorName = evaluatorName; }
    public String getTargetUserName() { return targetUserName; }
    public void setTargetUserName(String targetUserName) { this.targetUserName = targetUserName; }
}
