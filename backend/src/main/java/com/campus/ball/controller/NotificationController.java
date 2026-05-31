package com.campus.ball.controller;

import com.campus.ball.auth.AuthContext;
import com.campus.ball.entity.Notification;
import com.campus.ball.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@Api(tags = "通知接口")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    @ApiOperation("获取通知列表")
    public NotificationService.Result<List<Notification>> getNotifications() {
        Long userId = AuthContext.getUser().getId();
        return notificationService.getNotifications(userId);
    }

    @GetMapping("/unread-count")
    @ApiOperation("获取未读通知数量")
    public NotificationService.Result<?> getUnreadCount() {
        Long userId = AuthContext.getUser().getId();
        return notificationService.getUnreadCount(userId);
    }

    @PostMapping("/read/{id}")
    @ApiOperation("标记通知为已读")
    public NotificationService.Result<?> markAsRead(@PathVariable Long id) {
        Long userId = AuthContext.getUser().getId();
        return notificationService.markAsRead(userId, id);
    }

    @PostMapping("/read-all")
    @ApiOperation("标记所有通知为已读")
    public NotificationService.Result<?> markAllAsRead() {
        Long userId = AuthContext.getUser().getId();
        return notificationService.markAllAsRead(userId);
    }
}
