package com.zereao.util;

import com.zereao.chatroom.util.PackageUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Zereao
 * @version 2018/08/09 23:59
 */

class ServerTestUtil {
    @Test
    void test1() {
        List<String> a = PackageUtil.getAllClassName("org.junit.jupiter.api");
    }
}
