package com.zereao.chatroom.app;

import com.zereao.chatroom.annotation.PackageScan;
import com.zereao.chatroom.resolver.AnnoResolver;

/**
 * 服务启动类
 *
 * @author Zereao
 * @version 2018/08/10 0:01
 */
@PackageScan("com.zereao.service")
public class ServerApp {
    public static void main(String[] args) {
        // 启动注解处理器，开始bean的装配
        AnnoResolver.run(ServerApp.class);


    }
}
