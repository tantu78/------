package com.campus.ball.controller;

import com.campus.ball.common.Result;
import com.campus.ball.entity.Activity;
import com.campus.ball.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@Api(tags = "活动管理接口")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    @ApiOperation("创建活动")
    public Result<Activity> createActivity(@RequestBody Activity activity) {
        return activityService.createActivity(activity);
    }

    @PostMapping("/join/{id}")
    @ApiOperation("加入活动")
    public Result<Void> joinActivity(@PathVariable Long id) {
        return activityService.joinActivity(id);
    }

    @PostMapping("/quit/{id}")
    @ApiOperation("退出活动")
    public Result<Void> quitActivity(@PathVariable Long id) {
        return activityService.quitActivity(id);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询活动列表")
    public Result<Page<Activity>> listActivities(
            @RequestParam(required = false) String sportType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return activityService.listActivities(sportType, pageable);
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("获取活动详情")
    public Result<Activity> getActivityDetail(@PathVariable Long id) {
        return activityService.getActivityDetail(id);
    }

    @GetMapping("/my")
    @ApiOperation("获取我参与的活动")
    public Result<List<Activity>> getMyActivities() {
        return activityService.listMyActivities();
    }

    @GetMapping("/venue/{venueId}")
    @ApiOperation("根据场地查询活动")
    public Result<List<Activity>> getActivitiesByVenue(@PathVariable Long venueId) {
        return activityService.listActivitiesByVenue(venueId);
    }

    @GetMapping("/recommend")
    @ApiOperation("根据用户喜好推荐活动")
    public Result<List<Activity>> getRecommendActivities() {
        return activityService.recommendByFavorite();
    }

    @GetMapping("/participants/{id}")
    @ApiOperation("获取活动参与者列表")
    public Result<?> getParticipants(@PathVariable Long id) {
        return activityService.getParticipants(id);
    }
}
