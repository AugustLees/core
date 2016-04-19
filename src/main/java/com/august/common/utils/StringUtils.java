package com.august.common.utils;

import com.august.common.SpringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.utils
 * Author: August
 * Update: August(2015/10/28)
 * Description:字符串工具类
 * 该类继承org.springframework.util.StringUtils类，
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{
    public static final String EMPTY = "";
    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";

    public static boolean endsWithIgnoreCase(String key, String password) {
        return false;
    }


    /**
     * 转换为字符串数据组
     *
     * @param str 需要转换为字符串数组的字符串
     * @return
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 转换字节数组为字符串
     *
     * @param bytes 需要转换的字节
     * @return 字符串
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 是否包含字符串
     *
     * @param str     验证字符串
     * @param strings 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strings) {
        if (str != null) {
            for (String s : strings) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 替换掉HTML标签方法
     *
     * @param html 需要替换的字符串
     * @return
     */
    public static String replaceHtml(String html) {
        if (isEmpty(html)) {
            return EMPTY;
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        return m.replaceAll(EMPTY);
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html 需要替换格式的字符串
     * @return 手机可识别的HTML代买
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return EMPTY;
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param txt 需要替换的字符串
     * @return
     */
    public static String toHtml(String txt) {
        if (txt == null) {
            return EMPTY;
        }
        return replace(replace(StringEscapeUtils.escapeHtml4(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }

    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return EMPTY;
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return EMPTY;
    }

    /**
     * 缩略字符串（区分中英文字符）
     *
     * @param param  目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr2(String param, int length) {
        if (param == null) {
            return EMPTY;
        }
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHTML = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHTML) {
                isHTML = false;
            }
            try {
                if (!isCode && !isHTML) {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (n <= length - 3) {
                result.append(temp);
            } else {
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
                "$1$2");
        // 去掉不需要结素标记的HTML标记
        temp_result = temp_result
                .replaceAll(
                        "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                        "");
        // 去掉成对的HTML标记
        temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>",
                "$2");
        // 用正则表达式取出标记
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
        Matcher m = p.matcher(temp_result);
        List<String> endHTML = new ArrayList<String>();
        while (m.find()) {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * 转换为Double类型
     *
     * @param val 需要转换为对象
     * @return 转换后的结果
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     *
     * @param val 需要转换为对象
     * @return 转换后的结果
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     *
     * @param val 需要转换为对象
     * @return 转换后的结果
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     *
     * @param val 需要转换为对象
     * @return 转换后的结果
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    /**
     * 获得i18n字符串
     */
    public static String getMessage(String code, Object[] args) {
        LocaleResolver localLocaleResolver = SpringUtils.getBean(LocaleResolver.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Locale localLocale = localLocaleResolver.resolveLocale(request);
        return SpringUtils.getApplicationContext().getMessage(code, args, localLocale);
    }

    /**
     * 获得用户远程地址
     *
     * @param request 请求信息
     * @return IP地址信息
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String remoteAddress = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddress)) {
            remoteAddress = request.getHeader("X-Forwarded-For");
        } else if (isNotBlank(remoteAddress)) {
            remoteAddress = request.getHeader("Proxy-Client-IP");
        } else if (isNotBlank(remoteAddress)) {
            remoteAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddress != null ? remoteAddress : request.getRemoteAddr();
    }

    /**
     * 驼峰命名法工具（首字母小写）
     * 将字符串中的下划线去掉并将下一字母首字母大写
     * 例如：toCamelCase("hello_world") == "helloWorld"
     *
     * @param str 需要转换驼峰命名的字符串
     * @return
     */
    public static String toCamelCase(String str) {
        if (str == null) {
            return null;
        }

        //首先将所有字母转换为小写
        str = str.toLowerCase();

        StringBuilder sb = new StringBuilder(str.length());
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            //当该字节为下划线时，设定upperCase，当下次循环时，将该字节大写后添加到字符串中
            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                //否则直接添加到字符串中
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具（首字母大写）
     * 将需要驼峰命名的字符串进行转换，并将首字母大写
     * 例如：toCapitalizeCamelCase("hello_world") == "HelloWorld"
     *
     * @param str 需要转换驼峰命名的字符串
     * @return 转换后的字符串
     */
    public static String toCapitalizeCamelCase(String str) {
        if (str == null) {
            return null;
        }
        //先将下划线后的字母转换为大写
        str = toCamelCase(str);
        //将首字母转换为大写
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 驼峰命名法工具
     * 变更传入字符串为驼峰命名方式
     * 例如：toUnderScoreCase("helloWorld") = "hello_world"
     *
     * @param str 需要转换驼峰命名的字符串
     * @return 驼峰命名的字符串
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        //遍历字符串中的字节
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            boolean nextUpperCase = true;

            //判断下一个字母是不是大写
            if (i < (str.length() - 1)) {
                nextUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            //当前字母是大写的话
            if ((i > 0) && Character.isUpperCase(c)) {
                //当前字母不是大写，下一个字母不是大写，则在当前字母前添加下划线
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 如果不为空，则设置值
     *
     * @param target 目标值对象
     * @param source 源对象
     */
    public static void setValueIfNotBlank(String target, String source) {
        if (!isEmpty(source)) {
            target = source;
        }
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串
     *                     例如：row.user.id
     *                     返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] values = split(objectString, ".");
        for (int i = 0; i < values.length; i++) {
            val.append("." + values[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }
}
