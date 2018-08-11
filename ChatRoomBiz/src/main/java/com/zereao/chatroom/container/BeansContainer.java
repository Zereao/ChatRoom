package com.zereao.chatroom.container;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
            if (singletonContainer == null) {
                singletonContainer = new BeansContainer();
            }
        }
    }

    public static BeansContainer getInstance() {
        return Singleton.INSTANCE.singletonContainer;
    }

    // 将所有的被@Service注释的bean存入这个Map中
    private static Map<String, Object> serviceBeansMap = new ConcurrentHashMap<>();
    // BeanContainer的容量
    private int size;

    public void put(String name, Object obj) {
        serviceBeansMap.put(name, obj);
        ++size;
    }

    public Object get(String name) {
        return serviceBeansMap.get(name);
    }

    public Set<String> getAllKeys() {
        return serviceBeansMap.keySet();
    }

    public void clear() {
        serviceBeansMap.clear();
        size = 0;
    }

    public int size() {
        return size;
    }

}
