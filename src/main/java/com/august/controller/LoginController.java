package com.august.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.controller
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

}
