package com.zereao.chatroom.entry;

import java.io.Serializable;

/**
 * 顶级Message类
 *
 * @author Zereao
 * @version 2018/08/09 21:50
 */
public class Message implements Serializable {
    // 消息从哪个用户发送过来的
    protected User fromUser;
    // 消息将要发送给哪个用户
    protected User targetUser;

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
}
