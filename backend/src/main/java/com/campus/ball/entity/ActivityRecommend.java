package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "activity_recommend")
@ApiModel(value = "活动推荐实体")
public class ActivityRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID")
    private Long id;
    
    @Column(name = "sender_id", nullable = false)
    @ApiModelProperty(value = "发送者ID")
    private Long senderId;
    
    @Column(name = "receiver_id", nullable = false)
    @ApiModelProperty(value = "接收者ID")
    private Long receiverId;
    
    @Column(name = "activity_id", nullable = false)
    @ApiModelProperty(value = "活动ID")
    private Long activityId;
    
    @Column(name = "is_read")
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead = false;
    
    @Column(name = "create_time")
    @ApiModelProperty(value = "推荐时间")
    private LocalDateTime createTime;
    
    @Transient
    @ApiModelProperty(value = "发送者名称，查询时返回")
    private String senderName;
    
    @Transient
    @ApiModelProperty(value = "活动标题，查询时返回")
    private String activityTitle;
    
    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
    }
}
