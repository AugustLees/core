package com.august.controller;

import com.august.domain.hibernate.User;
import com.august.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.controller
 * Author: August
 * Update: August(2015/9/9)
 * Description:
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "getUserList")
    public List<User> getUserList() {
        LOGGER.debug("===========================================》接收请求");
        List<User> users = this.userService.getUserList();
        return users;
    }

    @ResponseBody
    @RequestMapping(value = "addUser")
    public String addUser() {
        LOGGER.debug("===========================================》接收请求");
        User user = new User();
        user.setName("测试员工1" + new Date().getTime());
        user.setEmail("邮件地址");
        user.setBirthday(new Date());
        user.setPassword("密码测试");
        userService.addUser(user);
        return "添加成功";
    }

}
