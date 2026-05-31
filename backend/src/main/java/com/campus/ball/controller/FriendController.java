package com.campus.ball.controller;

import com.campus.ball.auth.AuthContext;
import com.campus.ball.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friend")
@Api(tags = "好友接口")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/add")
    @ApiOperation("添加好友")
    public FriendService.Result<?> addFriend(@RequestParam Long friendId) {
        Long userId = AuthContext.getUser().getId();
        return friendService.addFriend(userId, friendId);
    }

    @PostMapping("/accept/{id}")
    @ApiOperation("接受好友申请")
    public FriendService.Result<?> acceptFriend(@PathVariable Long id) {
        Long userId = AuthContext.getUser().getId();
        return friendService.acceptFriend(userId, id);
    }

    @PostMapping("/reject/{id}")
    @ApiOperation("拒绝好友申请")
    public FriendService.Result<?> rejectFriend(@PathVariable Long id) {
        Long userId = AuthContext.getUser().getId();
        return friendService.rejectFriend(userId, id);
    }

    @GetMapping("/list")
    @ApiOperation("获取好友列表")
    public FriendService.Result<?> getFriends() {
        Long userId = AuthContext.getUser().getId();
        return friendService.getFriends(userId);
    }

    @GetMapping("/activities")
    @ApiOperation("获取好友的活动")
    public FriendService.Result<?> getFriendActivities() {
        Long userId = AuthContext.getUser().getId();
        return friendService.getFriendActivities(userId);
    }

    @GetMapping("/requests")
    @ApiOperation("获取待处理的好友申请")
    public FriendService.Result<?> getPendingRequests() {
        Long userId = AuthContext.getUser().getId();
        return friendService.getPendingRequests(userId);
    }
}
