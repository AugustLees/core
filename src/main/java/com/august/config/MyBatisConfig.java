package com.august.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/8/23)
 * Description:MyBatis数据库相关配置
 */
//@Configuration//表示该类是一个配置文件 //标注此类为配置类（必有）//启用注解事务管理，使用CGLib代理
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = {"com.august.domain**"})//设置mapper扫描基本路径，自动引入实体类
@Import(DataSourceConfig.class)
public class MyBatisConfig {
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisConfig.class);

    @Value("${hibernateShow_sql}")
    String hibernate_show_sql;
    @Resource(name = "myDruidDataSource")
    DruidDataSource druidDataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        System.out.println("MyBatisConfig中初始化SQLSessionFactory……");
        LOGGER.debug("MyBatisConfig中初始化SQLSessionFactory…………");
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //指定数据源
        sqlSessionFactoryBean.setDataSource(druidDataSource);
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置mybatis扫描配置信息，设置基本扫描路径，和指定SQLSessionFactory工厂
     *
     * @return
     * @throws SQLException
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() throws SQLException {
        System.out.println("MybatisConfig中映射文件扫描配置……");
        LOGGER.debug("MybatisConfig中中映射文件扫描配置……");
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //配置MyBatis扫描实体类基本路径
        mapperScannerConfigurer.setBasePackage("com.august.**.domain.mybatis.**");
        //指定SQL会话工厂bean名称
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfigurer;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager datasourcetransactionManager() throws SQLException {
        System.out.println("MybatisConfig中中配置事务管理器……");
        LOGGER.debug("MybatisConfig中中配置事务管理器……");
        DataSourceTransactionManager dataSourcetransactionManager = new DataSourceTransactionManager();
        dataSourcetransactionManager.setDataSource(druidDataSource);
        return dataSourcetransactionManager;
    }

}
