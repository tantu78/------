package com.campus.ball.service;

import com.campus.ball.entity.Activity;
import com.campus.ball.entity.FriendRelation;
import com.campus.ball.entity.User;
import com.campus.ball.repository.ActivityParticipantRepository;
import com.campus.ball.repository.ActivityRepository;
import com.campus.ball.repository.FriendRelationRepository;
import com.campus.ball.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRelationRepository friendRelationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityParticipantRepository activityParticipantRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private NotificationService notificationService;

    public Result<?> addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            return Result.error("不能添加自己为好友");
        }
        
        if (friendRelationRepository.existsByUserIdAndFriendIdAndStatus(userId, friendId, "accepted")) {
            return Result.error("你们已经是好友了");
        }
        
        if (friendRelationRepository.existsByUserIdAndFriendIdAndStatus(userId, friendId, "pending")) {
            return Result.error("已经发送过好友申请了");
        }
        
        User friend = userRepository.findById(friendId).orElse(null);
        if (friend == null) {
            return Result.error("用户不存在");
        }
        
        FriendRelation relation = new FriendRelation();
        relation.setUserId(userId);
        relation.setFriendId(friendId);
        relation.setStatus("pending");
        friendRelationRepository.save(relation);
        
        notificationService.createFriendRequest(userId, friendId, relation.getId());
        
        return Result.success("好友申请已发送");
    }

    public Result<?> acceptFriend(Long userId, Long relationId) {
        FriendRelation relation = friendRelationRepository.findById(relationId).orElse(null);
        if (relation == null) {
            return Result.error("申请不存在");
        }
        
        if (!relation.getFriendId().equals(userId)) {
            return Result.error("只有接收方才能接受申请");
        }
        
        if (!"pending".equals(relation.getStatus())) {
            return Result.error("该申请已处理");
        }
        
        relation.setStatus("accepted");
        friendRelationRepository.save(relation);
        
        FriendRelation reverseRelation = new FriendRelation();
        reverseRelation.setUserId(userId);
        reverseRelation.setFriendId(relation.getUserId());
        reverseRelation.setStatus("accepted");
        friendRelationRepository.save(reverseRelation);
        
        notificationService.createFriendAccept(userId, relation.getUserId());
        
        return Result.success("已同意好友申请");
    }

    public Result<?> rejectFriend(Long userId, Long relationId) {
        FriendRelation relation = friendRelationRepository.findById(relationId).orElse(null);
        if (relation == null) {
            return Result.error("申请不存在");
        }
        
        if (!relation.getFriendId().equals(userId)) {
            return Result.error("只有接收方才能拒绝申请");
        }
        
        relation.setStatus("rejected");
        friendRelationRepository.save(relation);
        
        return Result.success("已拒绝好友申请");
    }

    public Result<?> getFriends(Long userId) {
        List<FriendRelation> relations = friendRelationRepository.findByUserIdAndStatus(userId, "accepted");
        
        List<Long> friendIds = relations.stream()
            .map(FriendRelation::getFriendId)
            .collect(Collectors.toList());
        
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendIds) {
            userRepository.findById(friendId).ifPresent(friends::add);
        }
        
        return Result.success(friends);
    }

    public Result<?> getFriendActivities(Long userId) {
        List<FriendRelation> relations = friendRelationRepository.findByUserIdAndStatus(userId, "accepted");
        
        List<Long> friendIds = relations.stream()
            .map(FriendRelation::getFriendId)
            .collect(Collectors.toList());
        
        List<Activity> activities = new ArrayList<>();
        for (Long friendId : friendIds) {
            List<Activity> friendActivities = activityRepository.findByOrganizerId(friendId);
            activities.addAll(friendActivities);
        }
        
        return Result.success(activities);
    }

    public Result<?> getPendingRequests(Long userId) {
        List<FriendRelation> requests = friendRelationRepository.findByFriendIdAndStatus(userId, "pending");
        
        for (FriendRelation request : requests) {
            userRepository.findById(request.getUserId()).ifPresent(user -> {
                request.setFriendName(user.getUsername());
            });
        }
        
        return Result.success(requests);
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

        public static <T> Result<T> success(String message) {
            Result<T> result = new Result<>();
            result.code = 200;
            result.message = message;
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
