package com.tencent.wxcloudrun.exception;

public class MessageNotFoundException extends BusinessException {

    public MessageNotFoundException() {
        super(ErrorCode.MESSAGE_NOT_FOUND);
    }

    public MessageNotFoundException(String message) {
        super(ErrorCode.MESSAGE_NOT_FOUND, message);
    }
}