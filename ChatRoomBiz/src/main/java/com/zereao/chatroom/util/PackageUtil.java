package com.zereao.chatroom.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
            if ("file".equals(type)) {
                classList = getClassNameByFile(url.getPath(), packageName, isContainChildPackage);
            } else if (("jar").equals(type)) {

            }
        } else {

        }

        return null;
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
        List<String> classNameList = new ArrayList<>();
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
                        classNameList.addAll(tempList);
                    }
                }
            } else {
                String childFilePath = childFile.getPath();
                logger.info("");
                logger.info("======= 1 ======> {}", childFilePath);
                if (childFilePath.endsWith(".class")) {
                    /*
                      例如：childFilePath =
                      E:\JavaCode\ChatRoom\ChatRoomServer\target\classes\com\zereao\chatroom\app\ServerApp.class
                      经过下面这一步处理过后，得到结果 com\zereao\chatroom\app\ServerApp
                     */
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    logger.info("======= 2 ======> {}", childFilePath);
                    childFilePath = childFilePath.replace("\\", ".");
                    logger.info("======= 3 ======> {}", childFilePath);
                    childFilePath = childFilePath.substring(childFilePath.indexOf(packageName));
                    logger.info("======= 4 ======> {}", childFilePath);
                    logger.info("");
                    classNameList.add(childFilePath);
                }
            }
        }
        return classNameList;
    }
}
