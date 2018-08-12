package com.zereao.chatroom.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 处理各种文件、包、路径的工具类
 *
 * @author Zereao
 * @version 2018/08/10 22:31
 */
public class PackageUtil {
    private static final Logger logger = LoggerFactory.getLogger(PackageUtil.class);

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath         文件路径
     * @param icluChildPackage 是否遍历子包
     * @return 类的完整名称
     */
    public static List<String> getClassNameByFile(String filePath, boolean icluChildPackage) {
        logger.info("=========>  start!  filePath = {} , icluChildPackage = {}",
                filePath, icluChildPackage);
        List<String> classList = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null || childFiles.length <= 0) {
            return null;
        }
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (icluChildPackage) {
                    List<String> tempList = getClassNameByFile(childFile.getPath(), true);
                    if (tempList != null && tempList.size() > 0) {
                        classList.addAll(tempList);
                    }
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    /*
                      例如：childFilePath =
                      E:\JavaCode\ChatRoom\ChatRoomServer\target\classes\com\zereao\chatroom\app\ServerApp.class
                      经过下面这一步处理过后，得到结果 com\zereao\chatroom\app\ServerApp
                     */
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    classList.add(childFilePath);
                }
            }
        }
        return classList;
    }

    public static List<String> getClassNameByJar(String jarPath, boolean icluChildPackage) {
        logger.info("=========>  start!  jarPath = {} , icluChildPackage = {}",
                jarPath, icluChildPackage);
        List<String> classList = new ArrayList<>();
        /*
        例如，走到这里时，jarPath =
              file:/E:/CodeTools/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar!/org/junit/jupiter/api
         */
        String[] jarInfo = jarPath.split("!");
        /*
        经过以下处理，最终得到jarFilePath =
            E:/CodeTools/repository/org/junit/jupiter/junit-jupiter-api/5.2.0/junit-jupiter-api-5.2.0.jar
         */
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/") + 1);
        /*
        经过以下处理，最终得到packagePath =
            org/junit/jupiter/api
         */
        String packagePath = jarInfo[1].substring(1);
        logger.info("----->   jarFilePath = {} , packagePath = {}", jarFilePath, packagePath);
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> jarEntrys = jarFile.entries();
            while (jarEntrys.hasMoreElements()) {
                JarEntry entry = jarEntrys.nextElement();
                /*
                String entryRealName = entry.getRealName();
                    如果JarEntry不表示多版本JarFile的版本化条目，或者未将JarFile配置为处理多版本jar文件，
                则此方法返回与getName()返回的名称相同的名称。

                经过下面的处理，得到的entryName =
                    META-INF/
                    META-INF/MANIFEST.MF
                    org/
                    org/junit/
                    org/junit/jupiter
                    org/junit/jupiter/api
                    org/junit/jupiter/api/AssertArrayEquals.class
                    org/junit/jupiter/api/AssertNotNull.class等。
                也就是说，得到的jarEntry，包括文件对象、文件夹对象。
                */
                String entryName = entry.getName();
                logger.debug("====== 1 =====>     entryName = {}", entryName);
                if (entryName.endsWith(".class")) {
                    if (icluChildPackage) {
                        logger.debug("====== 2 =====>     entryName = {} , packagePath = {}", entryName, packagePath);
                        // 筛选出packagePath包路径下的所有类
                        if (entryName.startsWith(packagePath)) {
                            // 经过以下处理，得到className = org.junit.jupiter.api.BeforeAll
                            String className = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            classList.add(className);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            String tempPackagePath = entryName.substring(0, index);
                            logger.debug("====== 2 =====>     packagePath = {} , tempPackagePath = {}", packagePath, tempPackagePath);
                            // 只要packagePath下的类，不要packagePath子包下的类
                            if (packagePath.equals(tempPackagePath)) {
                                String className = entryName.replace("/", ",").substring(0, entryName.lastIndexOf("."));
                                classList.add(className);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("------>   {}", e.getMessage());
        }
        return classList;
    }

    /**
     * 从当前项目依赖的所有的Jar文件中搜索该包，并获取该包下所有的类
     *
     * @param urls             当前URLClassLoader下所有的URL 数组
     * @param packagePath      包路径
     * @param icluChildPackage 是否包含子包
     * @return 类的完整名称List
     */
    public static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean icluChildPackage) {
        List<String> classList = null;
        if (urls != null) {
            classList = new ArrayList<>();
            for (URL url : urls) {
                String urlPath = url.getPath();
                String jarPath = urlPath + "!/" + packagePath;
                classList.addAll(getClassNameByJar(jarPath, icluChildPackage));
            }
        }
        return classList;
    }
}