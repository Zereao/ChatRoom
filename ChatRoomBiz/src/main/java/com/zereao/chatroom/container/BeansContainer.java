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
            singletonContainer = new BeansContainer();
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

    public Object getInherit(Class cls) {
        Object result = null;
        // 标记，继承关系，应当只有只一个子类/实现类
        boolean isOnlyOne = true;
        for (String key : serviceBeansMap.keySet()) {
            Object obj = serviceBeansMap.get(key);
            //noinspection unchecked
            if (cls.isAssignableFrom(obj.getClass())) {
                if (!isOnlyOne) {
                    throw new IllegalStateException(cls.getName() + "类/接口 应该只能有一个实现类，容器中查到了多个！");
                }
                result = obj;
                isOnlyOne = false;
            }
        }
        return result;
    }

    public Set<String> keySet() {
        return serviceBeansMap.keySet();
    }

    public void clear() {
        serviceBeansMap.clear();
        size = 0;
    }

    public int size() {
        return size;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String tag = ", ";
        for (String key : serviceBeansMap.keySet()) {
            Object obj = serviceBeansMap.get(key);
            String beanName = obj.getClass().getName();
            sb.append(tag).append(beanName);
        }
        return sb.toString().replaceFirst(tag, "");
    }
}
