package com.campus.ball.controller;

import com.campus.ball.auth.AuthContext;
import com.campus.ball.common.Result;
import com.campus.ball.entity.Activity;
import com.campus.ball.entity.User;
import com.campus.ball.entity.Venue;
import com.campus.ball.repository.ActivityRepository;
import com.campus.ball.repository.UserRepository;
import com.campus.ball.repository.VenueRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/activities")
    @ApiOperation("获取所有活动")
    public Result<List<Activity>> getAllActivities() {
        Result<?> check = requireAdmin();
        if (check != null) return (Result<List<Activity>>) check;

        return Result.success(activityRepository.findAll());
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
}
