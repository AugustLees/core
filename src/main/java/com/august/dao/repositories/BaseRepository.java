package com.august.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.io.Serializable;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.dao.mapper
 * Author: Administrator
 * Update: Administrator(2015/9/18)
 * Description:基本的JPA管理工具类
 */
//@Transactional(transactionManager = "JPA")
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T,ID> {
}
