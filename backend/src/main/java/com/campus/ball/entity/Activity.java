package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
@ApiModel(value = "活动实体")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "活动ID")
    private Long id;
    
    @Column(nullable = false, length = 100)
    @ApiModelProperty(value = "活动标题")
    private String title;
    
    @Column(name = "sport_type", nullable = false, length = 20)
    @ApiModelProperty(value = "运动类型")
    private String sportType;
    
    @Column(name = "start_time", nullable = false)
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;
    
    @Column(name = "venue_id", nullable = false)
    @ApiModelProperty(value = "场地ID")
    private Long venueId;
    
    @Column(name = "max_participants", nullable = false)
    @ApiModelProperty(value = "最大参与人数")
    private Integer maxParticipants;
    
    @Column(name = "current_participants", nullable = false)
    @ApiModelProperty(value = "当前参与人数")
    private Integer currentParticipants = 1;
    
    @Column(name = "organizer_id", nullable = false)
    @ApiModelProperty(value = "组织者ID")
    private Long organizerId;
    
    @Column(length = 1000)
    @ApiModelProperty(value = "活动描述")
    private String description;
    
    @Column(length = 255)
    @ApiModelProperty(value = "活动封面图片")
    private String coverImage;
    
    @Column(name = "is_public")
    @ApiModelProperty(value = "是否公开活动")
    private Boolean isPublic = true;
    
    @Column(name = "need_audit")
    @ApiModelProperty(value = "是否需要审核加入")
    private Boolean needAudit = false;
    
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "audit_status", length = 20)
    @ApiModelProperty(value = "审核状态：pending / published / rejected / offline")
    private String auditStatus = "pending";

    @Column(name = "reject_reason", length = 1000)
    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    @Column(name = "offline_reason", length = 1000)
    @ApiModelProperty(value = "下架原因")
    private String offlineReason;

    @Column(name = "start_reminder_sent")
    @ApiModelProperty(value = "开始提醒是否已发送")
    private Boolean startReminderSent = false;

    @Column(name = "end_reminder_sent")
    @ApiModelProperty(value = "结束提醒是否已发送")
    private Boolean endReminderSent = false;

    @Transient
    @ApiModelProperty(value = "场地名称，查询时返回")
    private String venueName;

    @Transient
    @ApiModelProperty(value = "组织者名称，查询时返回")
    private String organizerName;
    
    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getNeedAudit() {
        return needAudit;
    }

    public void setNeedAudit(Boolean needAudit) {
        this.needAudit = needAudit;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getOfflineReason() {
        return offlineReason;
    }

    public void setOfflineReason(String offlineReason) {
        this.offlineReason = offlineReason;
    }

    public Boolean getStartReminderSent() {
        return startReminderSent;
    }

    public void setStartReminderSent(Boolean startReminderSent) {
        this.startReminderSent = startReminderSent;
    }

    public Boolean getEndReminderSent() {
        return endReminderSent;
    }

    public void setEndReminderSent(Boolean endReminderSent) {
        this.endReminderSent = endReminderSent;
    }
}
