package com.zereao.chatroom.util;

import java.util.ResourceBundle;

/**
 * Properties属性处理工具类
 *
 * @author Zereao
 * @version 2018/08/09 23:55
 */
public class PropParseUtil {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("server_config/server-config");

    public static String SERVER_IP = resourceBundle.getString("server.ip");
    public static Integer SERVER_PORT = Integer.valueOf(resourceBundle.getString("server.port"));
}
