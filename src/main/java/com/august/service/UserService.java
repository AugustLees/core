package com.august.service;

import com.august.dao.repositories.UserRepository;
import com.august.domain.hibernate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.service
 * Author: August
 * Update: August(2015/9/9)
 * Description:测试用户服务接口
 */
@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;
    public List<User> getUserList() {
        List<User> users=new ArrayList<User>();
        for (int i=0;i<9;i++){
            User user=new User();
            user.setId(i);
            user.setBirthday(new Date());
            user.setEmail("Email地址");
            user.setPassword("密码测试");
            users.add(user);
        }
        return users;
//        return (List<User>) userRepository.findAll();
    }
}
