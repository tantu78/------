package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_participant")
@ApiModel(value = "活动参与实体")
public class ActivityParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID")
    private Long id;
    
    @Column(name = "activity_id", nullable = false)
    @ApiModelProperty(value = "活动ID")
    private Long activityId;
    
    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    
    @Column(name = "join_time")
    @ApiModelProperty(value = "加入时间")
    private LocalDateTime joinTime;
    
    @Column(name = "is_evaluated")
    @ApiModelProperty(value = "是否已评价")
    private Boolean isEvaluated = false;
    
    @Column(name = "status")
    @ApiModelProperty(value = "状态：pending审核中，accepted已通过，rejected已拒绝")
    private String status = "accepted";
    
    @Transient
    @ApiModelProperty(value = "用户名称，查询时返回")
    private String username;
    
    @PrePersist
    public void prePersist() {
        joinTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }

    public Boolean getIsEvaluated() {
        return isEvaluated;
    }

    public void setIsEvaluated(Boolean evaluated) {
        isEvaluated = evaluated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
