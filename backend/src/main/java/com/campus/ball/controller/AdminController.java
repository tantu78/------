package com.campus.ball.controller;

import com.campus.ball.auth.AuthContext;
import com.campus.ball.common.Result;
import com.campus.ball.entity.Activity;
import com.campus.ball.entity.ActivityAuditLog;
import com.campus.ball.entity.User;
import com.campus.ball.entity.Venue;
import com.campus.ball.repository.ActivityRepository;
import com.campus.ball.repository.UserRepository;
import com.campus.ball.repository.VenueRepository;
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
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Api(tags = "管理员后台接口")
public class AdminController {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VenueRepository venueRepository;

    private boolean isAdmin() {
        User user = AuthContext.getUser();
        return user != null && "admin".equals(user.getRole());
    }

    private Result<?> requireAdmin() {
        if (!isAdmin()) {
            return Result.error(403, "需要管理员权限");
        }
        return null;
    }

    @GetMapping("/users")
    @ApiOperation("获取所有用户")
    public Result<List<User>> getAllUsers() {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<List<User>>) check;

        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    @PutMapping("/user/{id}")
    @ApiOperation("修改用户")
    public Result<?> updateUser(@PathVariable Long id, @RequestBody User update) {
        Result<?> check = requireAdmin();
        if (check != null) return check;

        User user = userRepository.findById(id).orElse(null);
        if (user == null) return Result.error("用户不存在");

        if (update.getRole() != null) user.setRole(update.getRole());
        if (update.getPhone() != null) user.setPhone(update.getPhone());
        if (update.getDescription() != null) user.setDescription(update.getDescription());
        if (update.getFavoriteSport() != null) user.setFavoriteSport(update.getFavoriteSport());

        userRepository.save(user);
        return Result.success("用户已更新");
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation("删除用户")
    public Result<?> deleteUser(@PathVariable Long id) {
        Result<?> check = requireAdmin();
        if (check != null) return check;

        if (!userRepository.existsById(id)) {
            return Result.error("用户不存在");
        }

        userRepository.deleteById(id);
        return Result.success("用户已删除");
    }

    @DeleteMapping("/activity/{id}")
    @ApiOperation("删除活动")
    public Result<?> deleteActivity(@PathVariable Long id) {
        Result<?> check = requireAdmin();
        if (check != null) return check;

        if (!activityRepository.existsById(id)) {
            return Result.error("活动不存在");
        }

        activityRepository.deleteById(id);
        return Result.success("活动已删除");
    }

    @PostMapping("/venue")
    @ApiOperation("添加场地")
    public Result<Venue> addVenue(@RequestBody Venue venue) {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<Venue>) check;

        venueRepository.save(venue);
        return Result.success(venue);
    }

    @PutMapping("/venue/{id}")
    @ApiOperation("修改场地")
    public Result<?> updateVenue(@PathVariable Long id, @RequestBody Venue update) {
        Result<?> check = requireAdmin();
        if (check != null) return check;

        Venue venue = venueRepository.findById(id).orElse(null);
        if (venue == null) return Result.error("场地不存在");

        if (update.getName() != null) venue.setName(update.getName());
        if (update.getSportType() != null) venue.setSportType(update.getSportType());
        if (update.getMapX() != null) venue.setMapX(update.getMapX());
        if (update.getMapY() != null) venue.setMapY(update.getMapY());
        if (update.getCapacity() != null) venue.setCapacity(update.getCapacity());
        if (update.getDescription() != null) venue.setDescription(update.getDescription());

        venueRepository.save(venue);
        return Result.success("场地已更新");
    }

    @DeleteMapping("/venue/{id}")
    @ApiOperation("删除场地")
    public Result<?> deleteVenue(@PathVariable Long id) {
        Result<?> check = requireAdmin();
        if (check != null) return check;

        if (!venueRepository.existsById(id)) {
            return Result.error("场地不存在");
        }

        venueRepository.deleteById(id);
        return Result.success("场地已删除");
    }

    @GetMapping("/stats")
    @ApiOperation("获取统计信息")
    public Result<?> getStats() {
        Result<?> check = requireAdmin();
        if (check != null) return check;

        long userCount = userRepository.count();
        long activityCount = activityRepository.count();
        long venueCount = venueRepository.count();

        java.util.Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("userCount", userCount);
        stats.put("activityCount", activityCount);
        stats.put("venueCount", venueCount);

        return Result.success(stats);
    }

    @GetMapping("/activities")
    @ApiOperation("管理员获取活动列表（可按审核状态过滤）")
    public Result<Page<Activity>> getAdminActivities(
            @RequestParam(required = false) String auditStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<Page<Activity>>) check;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Activity> activities;
        if (auditStatus != null && !auditStatus.isEmpty()) {
            activities = activityRepository.findByAuditStatus(auditStatus, pageable);
        } else {
            activities = activityRepository.findAll(pageable);
        }

        // 填充信息
        activities.getContent().forEach(a -> {
            if (a.getVenueId() != null) {
                venueRepository.findById(a.getVenueId()).ifPresent(v -> a.setVenueName(v.getName()));
            }
            if (a.getOrganizerId() != null) {
                User u = userRepository.findById(a.getOrganizerId()).orElse(null);
                if (u != null) {
                    a.setOrganizerName(u.getUsername());
                }
            }
        });

        return Result.success(activities);
    }

    @GetMapping("/activity/{id}")
    @ApiOperation("获取活动详情（管理员）")
    public Result<Activity> getAdminActivityDetail(@PathVariable Long id) {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<Activity>) check;

        Activity activity = activityRepository.findById(id).orElse(null);
        if (activity == null) return Result.error("活动不存在");

        if (activity.getVenueId() != null) {
            venueRepository.findById(activity.getVenueId()).ifPresent(v -> activity.setVenueName(v.getName()));
        }
        if (activity.getOrganizerId() != null) {
            User u = userRepository.findById(activity.getOrganizerId()).orElse(null);
            if (u != null) {
                activity.setOrganizerName(u.getUsername());
            }
        }

        return Result.success(activity);
    }

    @GetMapping("/activity/{id}/logs")
    @ApiOperation("获取活动审核日志")
    public Result<List<ActivityAuditLog>> getAuditLogs(@PathVariable Long id) {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<List<ActivityAuditLog>>) check;

        return activityService.getAuditLogs(id);
    }

    @PostMapping("/activity/{id}/approve")
    @ApiOperation("审核通过活动")
    public Result<Activity> approveActivity(@PathVariable Long id) {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<Activity>) check;

        return activityService.approveActivity(id);
    }

    @PostMapping("/activity/{id}/reject")
    @ApiOperation("驳回活动")
    public Result<Activity> rejectActivity(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<Activity>) check;

        String reason = body.get("reason");
        return activityService.rejectActivity(id, reason);
    }

    @PostMapping("/activity/{id}/offline")
    @ApiOperation("下架活动")
    public Result<Activity> offlineActivity(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<Activity>) check;

        String reason = body.get("reason");
        return activityService.offlineActivity(id, reason);
    }

    @Autowired
    private ActivityService activityService;
}
