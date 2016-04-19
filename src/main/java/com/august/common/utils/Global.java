package com.august.common.utils;

import com.august.common.PropertiesLoader;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.utils
 * Author: August
 * Update: August(2016/4/11)
 * Description:
 */
@Description("定义全局配置类")
public class Global {
    //当前对象实例
    private static Global global = new Global();

    //保存全局属性值
    private static Map<String, String> map = new HashMap<String, String>();

    //属性文件加载对象
    private static PropertiesLoader propertiesLoader = new PropertiesLoader("config/application.properties");

    //设置显示/隐藏
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    //设置是/否
    public static final String YES = "1";
    public static final String NO = "0";

    //设置对/错
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    //设置文件上传基础虚拟路径
    public static final String USER_FILES_BASE_URL = "/userFiles";


    //获取当前对象实例
    public static Global getInstance() {
        return global;
    }

    /**
     * 根据参数获取对应的配置信息
     *
     * @param key 参数主键
     * @return 参数值
     */
    public static String getConfig(String key) {
        //从集合map中获取指定键对应的值
        String value = map.get(key);
        //如果集合中没有这个信息则需要从配置文件中查找对应的信息并放入map集合中
        if (value == null) {
            value = propertiesLoader.getProperty(key);
            map.put(key, value == null ? StringUtils.EMPTY : value);
        }
        return value;
    }

    /**
     * 获取管理端应用根路径
     *
     * @return 返回对应的根路径信息
     */
    public static String getAdminPath() {
        return getConfig("adminPath");
    }

    /**
     * 获取前段应用根路径
     *
     * @return 返回对应的前段的根路径信息
     */
    public static String getFrontPath() {
        return getConfig("frontPath");
    }

    /**
     * 获取URL后缀信息
     *
     * @return 返回对应的URL后缀信息
     */
    public static String getUrlSuffix() {
        return getConfig("urlSuffix");
    }

    /**
     * 是否是演示模式，演示模式下不允许修改用户，角色，密码，菜单，授权
     *
     * @return 返回对应的模式
     */
    public static Boolean isDemoMode() {
        String demoModel = getConfig("demoModel");
        return "true".equalsIgnoreCase(demoModel) || "1".equals(demoModel);
    }

    /**
     * 在修改系统用户名和角色是是否同步到Activity
     *
     * @return 返回判断结果
     */
    public static boolean isSynActivityIdentify() {
        String isSynActivityIdentify = getConfig("activity.isSynActivityIdentify");
        return "true".equalsIgnoreCase(isSynActivityIdentify) || "1".equals(isSynActivityIdentify);
    }

    /**
     * 根据所提供的域值获取对应的常量值
     *
     * @param field 需要获取的域值的主键
     * @return 对应的常量信息
     */
    public static Object getConstant(String field) {
        try {
            return Global.class.getField(field).get(null);
        } catch (Exception e) {
            //如果出现异常，则表示没有配置，此处暂时不做处理
        }
        return null;
    }

    /**
     * 获取文件上传的根路径
     *
     * @return 返回对应的跟路径信息
     */
    public static String getUserFilesBaseDir() {
        //获取用户上传文件根路径
        String dir = getConfig("userFiles.baseDir");
        //如果没有配置文件上传路径则获取当前路径信息
        if (StringUtils.isEmpty(dir))
            try {
                dir = ClassLoaderUtil.getExtendResource("/").toString();
            } catch (MalformedURLException e) {
                return "";
            }
        //如果不是以/结尾，则将路径添加/
        if (!dir.endsWith("/")) dir += "/";
        return dir;
    }

    /**
     * 获取项目工程路径
     *
     * @return 工程路径
     */
    public static String getProjectPath() {
        String projectPath = Global.getConfig("projectPath");
        //如果配置了工程路径，则直接返回，否则自动获取
        if (StringUtils.isNotBlank(projectPath))
            return projectPath;
        //自动获取路径文件
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null)
                while (true) {
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f == null || f.exists()) break;
                    if (file.getParentFile() != null) file = file.getParentFile();
                    else break;
                }
            projectPath = file.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }
}






























