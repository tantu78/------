package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "evaluation")
@ApiModel(value = "评价实体")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID")
    private Long id;
    
    @Column(name = "evaluator_id", nullable = false)
    @ApiModelProperty(value = "评价者ID")
    private Long evaluatorId;
    
    @Column(name = "target_user_id", nullable = false)
    @ApiModelProperty(value = "被评价者ID")
    private Long targetUserId;
    
    @Column(name = "activity_id", nullable = false)
    @ApiModelProperty(value = "活动ID")
    private Long activityId;
    
    @Column(name = "skill_score")
    @ApiModelProperty(value = "球技评分1-5")
    private Integer skillScore;
    
    @Column(name = "organize_score")
    @ApiModelProperty(value = "组织评分1-5")
    private Integer organizeScore;
    
    @Column(length = 500)
    @ApiModelProperty(value = "评价内容")
    private String content;
    
    @Column(name = "create_time")
    @ApiModelProperty(value = "评价时间")
    private LocalDateTime createTime;
    
    @Transient
    @ApiModelProperty(value = "评价者名称，查询时返回")
    private String evaluatorName;
    
    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
    }
}
