package com.august.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
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
@Configuration//标注此类为配置类（必有）
// 启用注解事务管理，使用CGLib代理
@EnableTransactionManagement(proxyTargetClass = true)
//如果使用了JavaConfig的方式启动Spring，
// 那么即使是ImportResource了XML文件也是无法让MapperScannerConfigurer扫描到的，
// 必须改为同样的JavaConfig方式,即如下方式
@MapperScan(basePackages = {"com.**.dao.**.mapper"})//设置mapper扫描基本路径，自动引入实体类
@Import(MySQLDataSourceConfig.class)
public class MyBatisConfig {
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisConfig.class);

    @Resource(name = "mySqlDruidDataSource")
    private DruidDataSource druidDataSource;

    /**
     * 配置事务管理器
     *
     * @return
     * @throws SQLException
     */
    @Bean(name = "myBatisTransactionManager")
    @Qualifier(value = "myBatis")
    public DataSourceTransactionManager datasourcetransactionManager() throws SQLException {
        System.out.println("MyBatisConfig 中 1、 配置事务管理器……");
        LOGGER.debug("MyBatisConfig 中 1、 配置事务管理器……");
        DataSourceTransactionManager dataSourcetransactionManager = new DataSourceTransactionManager();
        dataSourcetransactionManager.setDataSource(druidDataSource);
        return dataSourcetransactionManager;
    }

    /**
     * 初始化SQLSessionFactory
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "myBatisSqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        System.out.println("MyBatisConfig 中 2、 初始化SQLSessionFactory……");
        LOGGER.debug("MyBatisConfig 中 2、 初始化SQLSessionFactory…………");
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //指定数据源
        sqlSessionFactoryBean.setDataSource(druidDataSource);
        ///配置MyBatis实体类基本路径,扫描所有mapper文件
        sqlSessionFactoryBean.setTypeAliasesPackage("com.**.domain.mybatis");
        return sqlSessionFactoryBean;
    }

    /**
     * 自动初始化数据库
     *
     * @return
     */
    @Bean(name = "MyBatisConfigDataSourceInitializer")
    public DataSourceInitializer dataSourceInitializer() {
        LOGGER.debug("MyBatisConfig 中 10、配置 数据库初始化……");
        System.out.println("MyBatisConfig 中 10、数据库初始化");
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(druidDataSource);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("db/user.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        dataSourceInitializer.setEnabled(true);
        return dataSourceInitializer;
    }
}
