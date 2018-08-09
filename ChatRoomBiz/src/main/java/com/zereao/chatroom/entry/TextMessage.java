package com.zereao.chatroom.entry;

/**
 * 文本消息
 *
 * @author Zereao
 * @version 2018/08/09 21:55
 */
public class TextMessage extends Message {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
