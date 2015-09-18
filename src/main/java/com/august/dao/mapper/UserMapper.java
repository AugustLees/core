package com.august.dao.mapper;

import com.august.domain.hibernate.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.dao.mapper
 * Author: Administrator
 * Update: Administrator(2015/9/18)
 * Description:基于mybatis管理的数据库操作接口
 */
@Repository(value = "userMapper")
@Transactional(transactionManager = "myBatis")
public interface UserMapper {
    @Insert("insert into user(name,email,password,birthday) values(#{name},#{email},#{password},#{birthday})")
    void addUser(User user);

    @Delete("delete from user where id = #{id}")
    public int deleteById(int id);

    @Update("update user set name = #{name}, age = #{age} where id = #{id}")
    public int update(User user);

    @Select("select * from user where id = #{id}")
    public User getUserById(int id);

    @Select("select * from user")
    public List<User> getAllUsers();
}
