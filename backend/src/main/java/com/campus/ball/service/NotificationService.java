package com.campus.ball.service;

import com.campus.ball.entity.Activity;
import com.campus.ball.entity.Notification;
import com.campus.ball.entity.User;
import com.campus.ball.repository.ActivityRepository;
import com.campus.ball.repository.NotificationRepository;
import com.campus.ball.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public void createFriendRequest(Long fromUserId, Long toUserId, Long relationId) {
        User fromUser = userRepository.findById(fromUserId).orElse(null);
        if (fromUser == null) return;
        
        Notification notification = new Notification();
        notification.setUserId(toUserId);
        notification.setType("friend_request");
        notification.setContent(fromUser.getUsername() + " 申请添加你为好友");
        notification.setRelatedId(relationId);
        notificationRepository.save(notification);
    }

    public void createFriendAccept(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId).orElse(null);
        if (fromUser == null) return;
        
        Notification notification = new Notification();
        notification.setUserId(toUserId);
        notification.setType("friend_accept");
        notification.setContent(fromUser.getUsername() + " 已同意你的好友申请");
        notificationRepository.save(notification);
    }

    public void createActivityJoin(Long activityId, Long userId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null) return;
        
        User joinUser = userRepository.findById(userId).orElse(null);
        if (joinUser == null) return;
        
        Notification notification = new Notification();
        notification.setUserId(activity.getOrganizerId());
        notification.setType("activity_join");
        notification.setContent(joinUser.getUsername() + " 参加了你的活动：" + activity.getTitle());
        notification.setRelatedId(activityId);
        notificationRepository.save(notification);
    }

    public void createActivityRecommend(Long userId, Activity activity) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType("activity_recommend");
        notification.setContent("你感兴趣的活动：" + activity.getTitle());
        notification.setRelatedId(activity.getId());
        notificationRepository.save(notification);
    }

    public Result<List<Notification>> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreateTimeDesc(userId);
        return Result.success(notifications);
    }

    public Result<Map<String, Object>> getUnreadCount(Long userId) {
        int count = notificationRepository.countByUserIdAndIsRead(userId, false);
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        return Result.success(data);
    }

    public Result<?> markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification == null) {
            return Result.error("通知不存在");
        }
        
        if (!notification.getUserId().equals(userId)) {
            return Result.error("无权操作");
        }
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return Result.success();
    }

    public Result<?> markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadOrderByCreateTimeDesc(userId, false);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
        return Result.success();
    }

    public static class Result<T> {
        private int code;
        private String message;
        private T data;

        public static <T> Result<T> success(T data) {
            Result<T> result = new Result<>();
            result.code = 200;
            result.message = "操作成功";
            result.data = data;
            return result;
        }

        public static <T> Result<T> success() {
            Result<T> result = new Result<>();
            result.code = 200;
            result.message = "操作成功";
            return result;
        }

        public static <T> Result<T> error(String message) {
            Result<T> result = new Result<>();
            result.code = 400;
            result.message = message;
            return result;
        }

        public int getCode() { return code; }
        public void setCode(int code) { this.code = code; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }
}
