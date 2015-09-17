package com.august.service;

import com.august.dao.repositories.UserRepository;
import com.august.domain.hibernate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return (List<User>) userRepository.findAll();
    }
}
