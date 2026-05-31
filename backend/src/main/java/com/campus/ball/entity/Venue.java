package com.campus.ball.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "venue")
@ApiModel(value = "场地实体")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "场地ID")
    private Long id;
    
    @Column(nullable = false, length = 100)
    @ApiModelProperty(value = "场地名称")
    private String name;
    
    @Column(length = 255)
    @ApiModelProperty(value = "场地地址")
    private String address;
    
    @Column(name = "map_x")
    @ApiModelProperty(value = "地图上的X坐标")
    private Integer mapX;
    
    @Column(name = "map_y")
    @ApiModelProperty(value = "地图上的Y坐标")
    private Integer mapY;
    
    @Column(name = "sport_type", length = 20)
    @ApiModelProperty(value = "支持的运动类型")
    private String sportType;
    
    @ApiModelProperty(value = "容纳人数")
    private Integer capacity;
    
    @Column(length = 500)
    @ApiModelProperty(value = "场地介绍")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMapX() {
        return mapX;
    }

    public void setMapX(Integer mapX) {
        this.mapX = mapX;
    }

    public Integer getMapY() {
        return mapY;
    }

    public void setMapY(Integer mapY) {
        this.mapY = mapY;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
