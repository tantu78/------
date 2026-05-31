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
}
