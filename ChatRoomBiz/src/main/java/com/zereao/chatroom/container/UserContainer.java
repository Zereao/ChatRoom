package com.zereao.chatroom.container;

import com.zereao.chatroom.entry.User;

import java.util.HashSet;
import java.util.Set;

/**
 * 在线用户Cache，保存用户在线状态的容器
 *
 * @author Zereao
 * @version 2018/08/09 23:04
 */
public class UserContainer {

    /**
     * 私有构造，单例模式
     */
    private UserContainer() {

    }

    // 使用枚举来实现单例模式
    private enum Singleton {
        INSTANCE;
        private UserContainer singletonContainer;

        Singleton() {
            if (singletonContainer == null) {
                singletonContainer = new UserContainer();
            }
        }
    }

    /**
     * 单例模式，获取唯一的UserCache实例
     *
     * @return 单例UserCache实例
     */
    public static UserContainer getInstance() {
        return Singleton.INSTANCE.singletonContainer;
    }


    private Set<User> userSet = new HashSet<>();

    /**
     * 取得所有在线的用户
     *
     * @return 在线用户Set
     */
    public Set<User> getAllUser() {
        return this.userSet;
    }

    /**
     * 新增一个用户
     *
     * @param user 需要添加的用户对象
     * @return 是否添加成功 {@link Set#add(Object)}
     */
    public synchronized boolean addUser(User user) {
        return userSet.add(user);
    }
}

