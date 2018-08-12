package com.zereao.chatroom.resolver;

import com.zereao.chatroom.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * 资源文件处理类
 *
 * @author Zereao
 * @version 2018/08/11 16:45
 */
@SuppressWarnings("WeakerAccess")
public class ResourceResolver {
    private static final Logger logger = LoggerFactory.getLogger(ResourceResolver.class);

    /**
     * 获取某个包下（包括子包）所有的类的完整名称
     *
     * @param packageName 报名
     * @return 类的完整名称List
     */
    public static List<String> getAllClassName(String packageName) {
        logger.info("=========>  使用默认配置：containChildPackage = true");
        return getClassName(packageName, true);
    }

    /**
     * 获取某个包下的类
     *
     * @param packageName      包名
     * @param icluChildPackage 是否包含子包
     * @return 类的完整名称List
     */
    public static List<String> getClassName(String packageName, boolean icluChildPackage) {
        logger.info("=========>  start!  packageName = {} , icluChildPackage = {}", packageName, icluChildPackage);
        String packagePath = packageName.replace(".", "/");
        List<String> classList = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            logger.info("url.getProtocol() --------->  {}", type);
            if ("file".equals(type)) {
                classList = PackageUtil.getClassNameByFile(url.getPath(), icluChildPackage);
            } else if (("jar").equals(type)) {
                classList = PackageUtil.getClassNameByJar(url.getPath(), icluChildPackage);
            }
        } else {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            classList = PackageUtil.getClassNameByJars(urls, packagePath, icluChildPackage);
        }
        return classList;
    }

}
