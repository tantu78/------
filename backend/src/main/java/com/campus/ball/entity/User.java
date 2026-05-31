package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@ApiModel(value = "用户实体")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "用户ID")
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    @ApiModelProperty(value = "用户名")
    private String username;
    
    @Column(nullable = false, length = 100)
    @ApiModelProperty(value = "密码")
    private String password;
    
    @Column(length = 255)
    @ApiModelProperty(value = "头像")
    private String avatar;
    
    @Column(name = "favorite_sport", length = 200)
    @ApiModelProperty(value = "喜好的运动类型：basketball/ badminton/pingpong/football/other")
    private String favoriteSport;
    
    @Column(name = "current_location")
    @ApiModelProperty(value = "当前选择的场地ID")
    private Long currentLocation;
    
    @Column(length = 20)
    @ApiModelProperty(value = "手机号")
    private String phone;
    
    @Column(length = 500)
    @ApiModelProperty(value = "个人简介")
    private String description;
    
    @Column(length = 20, columnDefinition = "varchar(20) default 'user'")
    @ApiModelProperty(value = "角色：user普通用户，admin管理员")
    private String role = "user";
    
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFavoriteSport() {
        return favoriteSport;
    }

    public void setFavoriteSport(String favoriteSport) {
        this.favoriteSport = favoriteSport;
    }

    public Long getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Long currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
