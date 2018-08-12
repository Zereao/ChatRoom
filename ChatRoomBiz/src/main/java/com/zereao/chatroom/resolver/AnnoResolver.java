package com.zereao.chatroom.resolver;

import com.zereao.chatroom.annotation.AutoBean;
import com.zereao.chatroom.annotation.Autowired;
import com.zereao.chatroom.annotation.PackageScan;
import com.zereao.chatroom.annotation.Service;
import com.zereao.chatroom.container.BeansContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 注解处理类
 *
 * @author Zereao
 * @version 2018/08/10 22:22
 */
public class AnnoResolver {
    private final Logger logger = LoggerFactory.getLogger(AnnoResolver.class);

    /**
     * 入口方法，启动注解解析、Bean的管理等
     *
     * @param clz @PackageScan注解标注的类
     */
    public static void run(Class clz) {
        AnnoResolver resolver = new AnnoResolver();
        resolver.logger.debug("-----> 开始解析注解...");
        try {
            resolver.getAndCacheAllBean(clz);
        } catch (ClassNotFoundException e) {
            resolver.logger.error(e.getMessage());
        }

        resolver.injectBean();
        resolver.logger.debug("-----> 注解解析完毕。");
    }

    /**
     * 处理@PackageScan注解，扫描包，并且将扫到的bean写入容器中
     *
     * @param clz @PackageScan注解标注的类
     * @throws ClassNotFoundException Class.forName()  加载类异常
     */
    private void getAndCacheAllBean(Class clz) throws ClassNotFoundException {
        PackageScan packageScan = (PackageScan) clz.getAnnotation(PackageScan.class);
        String packageName = packageScan.value();
        logger.debug("-----> 开始扫描{}包下所有的bean...", packageName);
        List<String> classList = ResourceResolver.getAllClassName(packageName);
        BeansContainer beansContainer = BeansContainer.getInstance();
        for (String className : classList) {
            Class cls = Class.forName(className);
            // 不能实例的类类型
            boolean isUnInstanceClass = cls.isInterface() || cls.isAnnotation() || cls.isEnum()
                    || cls.isLocalClass() || cls.isMemberClass() || cls.isAnonymousClass();
            if (isUnInstanceClass) {
                continue;
            }
            // 取得其所有的注解，迭代，用于注解扩展
            Annotation[] annotations = cls.getAnnotations();
            if (annotations == null || annotations.length < 1) {
                continue;
            }
            String annoValue = null;
            for (Annotation annotation : annotations) {
                if (annotation instanceof Service) {
                    Service service = (Service) annotation;
                    annoValue = service.value();
                    if (annoValue.trim().length() < 1) {
                        annoValue = cls.getSimpleName();
                    }
                    try {
                        //cls 是 使用class.forName()方法得到了，加载了并初始化了该类
                        @SuppressWarnings("unchecked")
                        Object obj = cls.getDeclaredConstructor().newInstance();
                        beansContainer.put(annoValue, obj);
                    } catch (Exception e) {
                        logger.error("----->  {} 调用默认无参构造函数失败！\n{}", className, e.getMessage());
                    }
                }
            }
        }
        logger.debug("-----> bean扫描完毕，并全部写入容器。\n{}", beansContainer.toString());
    }

    /**
     * 注入所有被@Service标注的bean
     */
    private void injectBean() {
        logger.debug("开始自动注入所有的bean...");
        BeansContainer beansContainer = BeansContainer.getInstance();
        for (String key : beansContainer.keySet()) {
            Object bean = beansContainer.get(key);
            inject(bean);
        }
    }

    /**
     * 开始自动注入所有实现了AutoBean接口的bean
     *
     * @param clz @PackageScan注解标注的类
     * @throws ClassNotFoundException Class.forName()加载类失败
     */
    private void injectAutoBean(Class clz) throws ClassNotFoundException {
        logger.debug("-----> 开始注入实现了AutoBean接口的类中的bean...");
        PackageScan packageScan = (PackageScan) clz.getAnnotation(PackageScan.class);
        String packageName = packageScan.value();
        logger.debug("-----> 开始扫描{}包下所有的bean...", packageName);
        List<String> classList = ResourceResolver.getAllClassName(packageName);
        for (String className : classList) {
            Class cls = Class.forName(className);
            // 如果该类被@Service注释，则不再注入bean
            if (cls.getAnnotation(Service.class) != null) {
                continue;
            }
            // 如果该类实现了AutoBean接口
            if (AutoBean.class.isAssignableFrom(cls)) {
                try {
                    @SuppressWarnings("unchecked")
                    Object bean = cls.getDeclaredConstructor().newInstance();
                    inject(bean);
                } catch (Exception e) {
                    logger.error("=====> bean - {}注入失败！\n{}", className, e.getMessage());
                }
            }
        }

    }

    /**
     * 实际的Bean的注入动作
     *
     * @param bean 需要注入的所有的bean所在的类的对象
     */
    private void inject(Object bean) {
        BeansContainer beansContainer = BeansContainer.getInstance();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class beanClass = field.getType();
            if (beanClass.isPrimitive()) {
                continue;
            }
            if (field.getAnnotation(Autowired.class) == null) {
                continue;
            }
            // 这里，不能用field.getName();
            String fieldName = beanClass.getSimpleName();
            Object fieldBean = beansContainer.get(fieldName);
            if (fieldBean != null) {
                try {
                    field.set(bean, fieldBean);
                } catch (IllegalAccessException e) {
                    logger.error("-----> 访问field-【{}】失败!\n{}", fieldName, e.getMessage());
                }
            } else {
                // 如果直接找搜索不到这个bean，则尝试着去找该类的子类对应的bean。
                try {
                    Object childBean = beansContainer.getInherit(beanClass);
                    field.set(bean, childBean);
                } catch (Exception e) {
                    logger.error("-----> 获取{}的子类bean失败！\n{}", bean.getClass().getSimpleName(), e.getMessage());
                }
            }
        }
    }
}
