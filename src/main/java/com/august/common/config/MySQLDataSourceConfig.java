package com.august.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.config
 * Author: August
 * Update: August(2015/8/26)
 * Description:数据源配置文件
 */
@Configuration
public class MySQLDataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLDataSourceConfig.class);
    /*
     * 绑定资源属性
     */
    //数据库地址
    @Value("${driverClassName:com.mysql.jdbc.Driver}")
    String driverClassName;
    //数据库地址
    @Value("${jdbc.url:jdbc:mysql://localhost:3306/core?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull}")
    String url;

    //数据库访问用户名
    @Value("${jdbc.username:root}")
    String userName;

    //数据库访问密码
    @Value("${jdbc.password:}")
    String password;

    //设置初始化连接大小
    @Value("${initialSize:5}")
    Integer initialSize;

    //设置连接池最大使用连接数量
    @Value("${maxActive:50}")
    Integer maxActive;

    //设置连接池最小空闲
    @Value("${minIdle:1}")
    Integer minIdle;

    //获取最大连接等待时间
    @Value("${maxWait:60000}")
    Long maxWait;

    //校验查询
    @Value("${validationQuery:SELECT 1}")
    String validationQuery;

    //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    @Value("${timeBetweenEvictionRunsMillis:60000}")
    Long timeBetweenEvictionRunsMillis;

    //配置一个连接在池中最小生存的时间，单位是毫秒
    @Value("${minEvictableIdleTimeMillis:25200000}")
    Long minEvictableIdleTimeMillis;


    //打开removeAbandoned功能时间
    @Value("${removeAbandonedTimeout:1800}")
    Integer removeAbandonedTimeout;

    /**
     * 使用druid数据源来初始化mybatis数据源
     *
     * @return
     * @throws SQLException
     */
    @Bean(name = "mySqlDruidDataSource", initMethod = "init", destroyMethod = "close")
    @Qualifier("mySqlDruidDataSource")
    public DataSource druidDataSource() throws SQLException {
        LOGGER.debug("MySQLDataSourceConfig 中 1、初始化数据源……");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setName("Mysql数据库");
        druidDataSource.setDbType("mysql");
        druidDataSource.setUrl(url);
//        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        // 配置初始化大小、最小、最大
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMinIdle(minIdle);
        //配置获取连接等待超时的时间
        druidDataSource.setMaxWait(maxWait);
        //验证连接有效与否的SQL，不同的数据配置不同
        druidDataSource.setValidationQuery(validationQuery);
        //这里建议配置为TRUE，防止取到的连接不可用
        druidDataSource.setTestOnBorrow(true);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        //配置一个连接在池中最小生存的时间，单位是毫秒
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        //打开removeAbandoned功能
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        //关闭abanded连接时输出错误日志
        druidDataSource.setLogAbandoned(true);
        //打开PSCache，并且指定每个连接上PSCache的大小
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        //这里配置提交方式，默认就是TRUE，可以不用配置
        druidDataSource.setDefaultAutoCommit(true);
        //监控数据库
//        属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：
//        监控统计用的filter:stat
//        日志用的filter:log4j
//        防御SQL注入的filter:wall
        druidDataSource.setFilters("mergeStat,log4j,wall");
        return druidDataSource;
    }
}
