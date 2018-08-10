package com.zereao.util;

import com.zereao.chatroom.entry.User;
import com.zereao.chatroom.container.UserContainer;
import com.zereao.chatroom.util.PackageUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Zereao
 * @version 2018/08/09 23:31
 */
class BizTestUtil {
    @Test
    void test2() {
        PackageUtil.getAllClassName("com.zereao.chatroom.util");
    }

    @Test
    void test1() {
        UserContainer container = UserContainer.getInstance();
        System.out.println(container.hashCode());

        User user = new User();
        user.setIp("1");
        User user1 = new User();
        user1.setIp("2");

        User user2 = user;

        System.out.println(container.addUser(user));
        System.out.println(container.getAllUser());
        System.out.println(container.addUser(user1));
        System.out.println(container.getAllUser());
        System.out.println(container.addUser(user2));
        System.out.println(container.getAllUser());
    }
}
