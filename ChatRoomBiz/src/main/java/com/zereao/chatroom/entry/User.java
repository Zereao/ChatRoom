package com.zereao.chatroom.entry;

import java.util.Objects;

/**
 * 用户对象类
 *
 * @author Zereao
 * @version 2018/08/09 21:51
 */
public class User {
    private static final long serialVersionUID = 1L;

    private String name;
    private String ip;
    private Integer port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(ip, user.ip) &&
                Objects.equals(port, user.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ip, port);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
