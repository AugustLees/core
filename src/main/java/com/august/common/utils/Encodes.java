package com.august.common.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.context.annotation.Description;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.utils
 * Author: August
 * Update: August(2016/4/18)
 * Description:封装各种格式的编码解码工具类
 * 2、自定义的base62编码
 * 3、Commons-lang的XML、html escape
 * 4、JDK提供的URLEncoder
 */
@Description("封装各种格式的编码解码工具类")
public class Encodes {
    //定义默认URL编码
    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    //定义base62编码的基本字节
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();


    /**
     * HEX编码
     * 对输入的内容进行HEX编码
     *
     * @param input 输入数据
     * @return 输出结果
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    /**
     * HEX解码
     * 对输入的内容进行HEX解码
     *
     * @param input 要解码的内容
     * @return 解码后的内容
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Base64编码
     *
     * @param input 要进行编码的数据
     * @return 编码后的字符串n
     */
    public static String encodeBase64(byte[] input) {
        return new String(Base64.encodeBase64(input));
    }

    /**
     * Base62编码
     *
     * @param input 需要进行编码的内容
     * @return 编码后的内容
     */
    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
        }
        return new String(chars);
    }

    /**
     * HTML转码
     *
     * @param html 需要进行编码的文件
     * @return 编码后的内容
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * HTML解码
     *
     * @param htmlEscaped 需要解码的HTML文件
     * @return 解码后的内容
     */
    public static String unescapeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * XML转码
     *
     * @param xml 需要进行编码的XML信息
     * @return 编码后的文件
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml11(xml);
    }

    /**
     * XML 解码
     *
     * @param xmlEscaped 需要解码的XML信息
     * @return 解码后的信息
     */
    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * URL 编码，Encode默认为UTF-8
     *
     * @param part 需要编码的部分
     * @return 编码后的内容
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * URL 解码 ，Encode默认为UTF-8
     *
     * @param part 需要解码的内容
     * @return 解码后的内容
     */
    public static String urlDecode(String part) {
        try {
            return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw Exceptions.unchecked(e);
        }
    }
}









































