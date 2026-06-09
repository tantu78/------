package com.campus.ball.service;

import com.campus.ball.auth.AuthContext;
import com.campus.ball.auth.TokenStore;
import com.campus.ball.common.Result;
import com.campus.ball.common.ResultCode;
import com.campus.ball.entity.User;
import com.campus.ball.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenStore tokenStore;

    public Result<User> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return Result.error(ResultCode.USERNAME_EXISTS);
        }
        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);
        return Result.success(savedUser);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password).orElse(null);
        if (user == null) {
            return null;
        }
        user.setPassword(null);
        return tokenStore.createToken(user);
    }

    public User findByCredentials(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password).orElse(null);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    public void logout(String token) {
        tokenStore.removeToken(token);
    }

    public Result<User> getCurrentUser() {
        User user = AuthContext.getUser();
        if (user == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        User fresh = userRepository.findById(user.getId()).orElse(null);
        return Result.success(fresh != null ? fresh : user);
    }

    public Result<User> updateUser(User updates, String token) {
        User user = AuthContext.getUser();
        if (user == null) {
            return Result.error(ResultCode.UNAUTHORIZED);
        }
        user.setPhone(updates.getPhone());
        user.setFavoriteSport(updates.getFavoriteSport());
        user.setDescription(updates.getDescription());
        user.setAvatar(updates.getAvatar());
        User saved = userRepository.save(user);
        saved.setPassword(null);
        tokenStore.updateTokenUser(token, saved);
        return Result.success(saved);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Result<List<User>> searchUsers(String keyword) {
        List<User> users = userRepository.findAll().stream()
            .filter(u -> u.getUsername().contains(keyword))
            .collect(Collectors.toList());
        
        users.forEach(u -> u.setPassword(null));
        
        return Result.success(users);
    }
}
