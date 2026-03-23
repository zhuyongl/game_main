package icu.xindongxuanxiang.game.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 用户相关错误
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    INVALID_USERNAME_OR_PASSWORD(1003, "用户名或密码错误"),
    WECHAT_USER_BINDING_FAILED(1004, "微信用户绑定失败"),

    // 留言相关错误
    MESSAGE_NOT_FOUND(2001, "留言不存在"),
    MESSAGE_CONTENT_EMPTY(2002, "留言内容不能为空"),
    MESSAGE_CONTENT_RISKY(2003, "留言内容包含违规信息"),

    // 微信内容安全相关错误
    WECHAT_SEC_CHECK_FAILED(4001, "微信内容安全检测失败"),
    WECHAT_SEC_CHECK_RISKY(4002, "内容包含违规信息"),
    WECHAT_ACCESS_TOKEN_ERROR(4003, "获取微信access_token失败"),

    // 参数验证错误
    PARAMETER_INVALID(3001, "参数无效"),
    PARAMETER_MISSING(3002, "缺少必要参数"),

    // 系统错误
    SYSTEM_ERROR(5000, "系统内部错误");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}