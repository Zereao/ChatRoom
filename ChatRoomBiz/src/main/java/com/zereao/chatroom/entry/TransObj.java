package com.zereao.chatroom.entry;

import java.io.Serializable;

/**
 * @author : Zereao
 * @version : 2018/8/12/14/31  14:31
 */
public class TransObj<T> implements Serializable {
    /**
     * 来自哪个用户
     */
    private User fromUser;
    /**
     * 发往哪个用户
     */
    private User targetUser;
    /**
     * 传输的对象
     */
    private T transObj;
    /**
     * TransObj的类型，传送的消息中的上下文类型
     */
    private TransObjType type;

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

    public T getTransObj() {
        return transObj;
    }

    public void setTransObj(T transObj) {
        this.transObj = transObj;
    }

    public TransObjType getType() {
        return type;
    }

    public void setType(TransObjType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TransObj{" +
                "fromUser=" + fromUser +
                ", targetUser=" + targetUser +
                ", transObj=" + transObj +
                ", type=" + type +
                '}';
    }
}
