package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friend_relation")
@ApiModel(value = "好友关系实体")
public class FriendRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @Column(name = "friend_id", nullable = false)
    @ApiModelProperty(value = "好友ID")
    private Long friendId;

    @ApiModelProperty(value = "状态：pending申请中，accepted已通过，rejected已拒绝")
    private String status;

    @Column(name = "create_time")
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;

    @Transient
    @ApiModelProperty(value = "好友名称，查询时返回")
    private String friendName;

    @Transient
    @ApiModelProperty(value = "好友头像，查询时返回")
    private String friendAvatar;

    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getFriendId() { return friendId; }
    public void setFriendId(Long friendId) { this.friendId = friendId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getFriendName() { return friendName; }
    public void setFriendName(String friendName) { this.friendName = friendName; }
    public String getFriendAvatar() { return friendAvatar; }
    public void setFriendAvatar(String friendAvatar) { this.friendAvatar = friendAvatar; }
}
