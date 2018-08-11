package com.zereao.chatroom.resolver;

import com.zereao.chatroom.annotation.PackageScan;
import com.zereao.chatroom.annotation.Service;
import com.zereao.chatroom.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 注解处理类
 *
 * @author Zereao
 * @version 2018/08/10 22:22
 */
public class AnnoResolver {
    private static final Logger logger = LoggerFactory.getLogger(AnnoResolver.class);

    public static void run(Class clz) {
        try {
            PackageScan packageScan = (PackageScan) clz.getAnnotation(PackageScan.class);
            String packageName = packageScan.value();
            List<String> classList = ResourceResolver.getAllClassName(packageName);
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

                    }
                }
                Object obj = null;
                try {
                    //noinspection unchecked
                    obj = cls.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    logger.error("----->  {} 调用默认无参构造函数失败！\n{}", className, e.getMessage());
                    continue;
                }
            }
        } catch (Exception e) {

        }
    }
}
