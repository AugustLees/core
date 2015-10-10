package com.august.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.dao.mapper
 * Author: Administrator
 * Update: Administrator(2015/9/18)
 * Description:基本的JPA管理工具类
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
}
