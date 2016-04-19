package com.august.modules.system.controller;

import com.august.common.utils.CacheUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.controller
 * Author: August
 * Update: August(2015/10/26)
 * Description: 登录控制器
 */
@Controller
public class LoginController {
    @RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {

        return null;
    }

    /**
     * 是否是验证码登录
     *
     * @param username 用户名
     * @param isFail   计数加1
     * @param clean    计数清零
     * @return 是否是验证码登录
     */
    public static boolean isValidateCodeLogin(String username, boolean isFail, boolean clean) {
        //从缓存中获取登录失败信息
        Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
        //如果没有登录失败的信息则创建一个空的map并放入到缓存中
        if (loginFailMap == null) {
            loginFailMap = new HashMap<String, Integer>();
            CacheUtils.put("loginFailMap", loginFailMap);
        }
        //获取该用户的登录失败次数
        Integer loginFailNum = loginFailMap.get(username);
        //如果没有失败次数，则默认失败次数为1
        if (loginFailNum == null) loginFailNum = 0;
        //如果确定允许计数增加，则增加对应的失败次数，并将失败次数放入到缓存中
        if (isFail) {
            loginFailNum++;
            loginFailMap.put(username, loginFailNum);
        }
        //如果要清除登录失败信息，则直接从缓存中移除该用户的失败信息
        if (clean)
            loginFailMap.remove(username);
        //是否大于3次登录
        return loginFailNum >= 3;
    }

}



















