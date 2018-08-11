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
     * @param packageName           包名
     * @param isContainChildPackage 是否包含子包
     * @return 类的完整名称List
     */
    public static List<String> getClassName(String packageName, boolean isContainChildPackage) {
        logger.info("=========>  start!  packageName = {} , isContainChildPackage = {}", packageName, isContainChildPackage);
        String packagePath = packageName.replace(".", "/");
        List<String> classList = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            logger.info("url.getProtocol() --------->  {}", type);
            if ("file".equals(type)) {
                classList = getClassNameByFile(url.getPath(), packageName, isContainChildPackage);
            } else if (("jar").equals(type)) {
                try {
                    classList = getClassNameByJar(url.getPath(), packageName, isContainChildPackage);
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }

            }
        } else {

        }

        return classList;
    }


    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath              文件路径
     * @param packageName           包名
     * @param isContainChildPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByFile(String filePath, String packageName, boolean isContainChildPackage) {
        logger.info("=========>  start!  filePath = {} , packageName = {} , isContainChildPackage = {}",
                filePath, packageName, isContainChildPackage);
        List<String> classList = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null || childFiles.length <= 0) {
            return null;
        }
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (isContainChildPackage) {
                    List<String> tempList = getClassNameByFile(childFile.getPath(), packageName, true);
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

    private static List<String> getClassNameByJar(String jarPath, String packageName, boolean isContainChildPackage) throws IOException {
        logger.info("=========>  start!  jarPath = {} , packageName = {} , isContainChildPackage = {}",
                jarPath, packageName, isContainChildPackage);
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
        try (
                JarFile jarFile = new JarFile(jarFilePath);
        ) {
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
                    if (isContainChildPackage) {
                        // 筛选出packageName包下的所有类
                        if (entryName.startsWith(packageName)) {
                            // 经过以下处理，得到className = org.junit.jupiter.api.BeforeAll
                            String className = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            classList.add(className);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            String tempPackagePath = entryName.substring(0, index);
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
            throw e;
        }

        return classList;
    }
}
