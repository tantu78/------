package com.campus.ball.auth;

import com.campus.ball.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AuthFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "auth_token";

    @Autowired
    private TokenStore tokenStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getCookieValue(request, COOKIE_NAME);
        User user = null;
        if (token != null && !token.isEmpty()) {
            user = tokenStore.getUser(token);
            if (user != null) {
                AuthContext.setUser(user);
            }
        }

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        if (uri.endsWith("/admin.html") || uri.endsWith("/admin-venues.html")) {
            if (user == null) {
                response.sendRedirect(ctx + "/login.html?redirect=" + java.net.URLEncoder.encode(uri, "UTF-8"));
                return;
            }
            if (!"admin".equals(user.getRole())) {
                response.sendRedirect(ctx + "/index.html");
                return;
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            AuthContext.clear();
        }
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

    public static void setCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    public static void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
