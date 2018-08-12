package com.zereao.chatroom.entry;

/**
 * 传送数据的类型枚举
 *
 * @author : Zereao
 * @version : 2018/8/12/14/24  14:24
 */
public enum TransObjType {
    /**
     * 用户登陆信息
     */
    LOGIN_MSG,
    /**
     * 用户注销信息
     */
    LOGOUT_MSG,
    /**
     * 用户发送的消息
     */
    MESSAGE,
    /**
     * 文件类型
     */
    FILE
}
