package com.campus.ball.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    SYSTEM_ERROR(500, "服务器内部错误"),
    USERNAME_EXISTS(1001, "用户名已存在"),
    USERNAME_PASSWORD_ERROR(1002, "用户名或密码错误"),
    ACTIVITY_FULL(2001, "活动人数已满"),
    ALREADY_JOINED(2002, "您已加入该活动"),
    NOT_JOINED(2003, "您未加入该活动"),
    CANNOT_QUIT_OWN_ACTIVITY(2004, "组织者不能退出自己的活动"),
    ALREADY_FRIENDS(3001, "你们已经是好友了"),
    FRIEND_REQUEST_EXISTS(3002, "好友申请已发送，请勿重复申请"),
    CANNOT_ADD_YOURSELF(3003, "不能添加自己为好友");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
