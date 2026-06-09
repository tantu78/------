package com.campus.ball.controller;

import com.campus.ball.auth.AuthFilter;
import com.campus.ball.common.Result;
import com.campus.ball.entity.User;
import com.campus.ball.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<User> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<User> login(@RequestParam String username, @RequestParam String password,
                              HttpServletResponse response) {
        User user = userService.findByCredentials(username, password);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        String token = userService.login(username, password);
        AuthFilter.setCookie(response, token);
        return Result.success(user);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = getCookieValue(request, "auth_token");
        if (token != null) {
            userService.logout(token);
        }
        AuthFilter.clearCookie(response);
        return Result.success();
    }

    @GetMapping("/current")
    @ApiOperation("获取当前登录用户信息")
    public Result<User> getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public Result<User> updateUser(@RequestBody User user, HttpServletRequest request) {
        String token = getCookieValue(request, "auth_token");
        return userService.updateUser(user, token);
    }

    @GetMapping("/search")
    @ApiOperation("搜索用户")
    public Result<?> searchUsers(@RequestParam String keyword) {
        return userService.searchUsers(keyword);
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
