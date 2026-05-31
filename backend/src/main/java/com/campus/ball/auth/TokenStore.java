package com.campus.ball.auth;

import com.campus.ball.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private final Map<String, User> tokenMap = new ConcurrentHashMap<>();

    public String createToken(User user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenMap.put(token, user);
        return token;
    }

    public User getUser(String token) {
        if (token == null) return null;
        return tokenMap.get(token);
    }

    public void removeToken(String token) {
        if (token != null) {
            tokenMap.remove(token);
        }
    }

    public void updateTokenUser(String token, User user) {
        tokenMap.put(token, user);
    }
}
