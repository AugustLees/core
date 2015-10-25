package com.august.service;

import com.august.dao.mapper.UserMapper;
import com.august.dao.repositories.UserRepository;
import com.august.domain.hibernate.User;
import com.august.common.JPATx;
import com.august.common.MyBatisTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

//    @Autowired
//    private UserMapper userMapper;

    /**
     * 利用mybatis管理的事务进行管理
     *
     * @return
     */
//    @MyBatisTx
    public List<User> getUserList() {
        List<User> users = (List<User>) userRepository.findAll();
//        List<User> users = (List<User>) userMapper.getAllUsers();
        System.out.println(users.size());
        return users;
    }

    @JPATx
    public void addUser(User user) {
//        userMapper.addUser(user);
        userRepository.save(user);
    }

    /**
     * 根据用户名查询该用户授权信息
     * @param username
     * @return
     */
    public  Collection<String> queryPermissions(String username) {
        return null;
    }

    public LoginAccount get(String username) {
        return null;
    }
}
