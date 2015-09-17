package com.august.controller;

import com.august.domain.hibernate.User;
import com.august.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "getUserList")
    public List<User> getUserList() {
        return this.userService.getUserList();
    }
}
