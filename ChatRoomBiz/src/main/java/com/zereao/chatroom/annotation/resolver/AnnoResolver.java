package com.zereao.chatroom.annotation.resolver;

import com.zereao.chatroom.annotation.PackageScan;

/**
 * 注解处理类
 *
 * @author Zereao
 * @version 2018/08/10 22:22
 */
public class AnnoResolver {
    public static void run(Class clz) {
        try {
            PackageScan packageScan = (PackageScan) clz.getAnnotation(PackageScan.class);
            String packageName = packageScan.value();
        } catch (Exception e) {

        }
    }
}
