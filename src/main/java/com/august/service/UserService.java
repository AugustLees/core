package com.august.service;

import com.august.dao.mapper.UserMapper;
import com.august.dao.repositories.UserRepository;
import com.august.domain.hibernate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.service
 * Author: August
 * Update: August(2015/9/9)
 * Description:测试用户服务接口
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<User> getUserList() {
//        List<User> users = (List<User>) userRepository.findAll();
        List<User> users = (List<User>) userMapper.getAllUsers();
        System.out.println(users.size());
        return users;
    }


    public void addUser(User user) {
//        userMapper.addUser(user);
        userRepository.save(user);
    }
}
