package com.zereao.chatroom.container;

import java.util.Map;

/**
 * Beans容器，所有被@Service注解标注的类，都放到该容器中
 *
 * @author Zereao
 * @version 2018/08/11 15:25
 */
public class BeansContainer {
    private BeansContainer() {
    }

    private enum Singleton {
        INSTANCE;
        private BeansContainer singletonContainer;

        Singleton() {
            singletonContainer = new BeansContainer();
        }
    }

    public static BeansContainer getInstance() {
        return Singleton.INSTANCE.singletonContainer;
    }


}
