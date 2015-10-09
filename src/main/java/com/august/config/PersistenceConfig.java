package com.august.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.august.utils.StaticConstant;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.config
 * Author: August
 * Update: August(2015/9/2)
 * Description:配置hibernate数据源相关信息
 * 该配置是基于JPA的数据库配置
 */
@Configuration
//启用注解事务管理，使用CGLib代理
@EnableTransactionManagement(proxyTargetClass = true)
//启用Jpa配置
//配置jpa扫描基本实现类,即数据库操作层相关实现
@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManager",
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        basePackages = StaticConstant.JPA_BASE_PACKAGES)
@Import(DataSourceConfig.class)
public class PersistenceConfig {
    //定义日志记录器
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceConfig.class);

    @Value("${hibernateDialect:org.hibernate.dialect.MySQLDialect}")
    String hibernate_dialect;
    @Value("${hibernateShow_sql:true}")
    String hibernate_show_sql;
    @Value("${hibernateFormat_sql:true}")
    String hibernate_format_sql;
    @Value("${hibernateHbm2ddlAuto:update}")
    String hibernate_hbm2ddl_auto;

    @Value("${init-db:false}")
    String initDatabase;

    @Resource(name = "myDruidDataSource")
    private DruidDataSource druidDataSource;

    //配置 JPA 提供商的适配器
    @Bean(name = "jpaVendorAdapter")
    public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() {
        LOGGER.debug("PersistenceConfig 中 配置 4、JPA 提供商的适配器……");
        System.out.println("PersistenceConfig 中 4、配置 JPA 提供商的适配器……");
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        hibernateJpaVendorAdapter.setGenerateDdl(Boolean.valueOf(hibernate_hbm2ddl_auto));
        hibernateJpaVendorAdapter.setShowSql(Boolean.valueOf(hibernate_show_sql));
        return hibernateJpaVendorAdapter;
    }

    /**
     * 配置jpa服务提供者
     *
     * @return
     */
    @Bean(name = "persistenceProvider")
    public HibernatePersistenceProvider hibernatePersistenceProvider() {
        LOGGER.debug("PersistenceConfig 中 2、配置 持久化类服务提供者……");
        System.out.println("PersistenceConfig 中 2、配置 持久化类服务提供者……");
        HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
        return hibernatePersistenceProvider;
    }

    /**
     * 定义jpa方言
     *
     * @return
     */
    @Bean(name = "jpaDialect")
    public HibernateJpaDialect hibernateJpaDialect() {
        LOGGER.debug("PersistenceConfig 中 3、配置 JPA方言……");
        System.out.println("PersistenceConfig 中 3、配置 JPA方言……");
        return new HibernateJpaDialect();
    }


    /**
     * 配置 EntityManagerFactory
     *
     * @return
     */
    @Bean(name = "jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory() {
        LOGGER.debug("PersistenceConfig 中 1、配置 EntityManagerFactory……");
        System.out.println("PersistenceConfig 中 1、配置 EntityManagerFactory……");
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(druidDataSource);

        //设置持久化单元名称
        localContainerEntityManagerFactoryBean.setPersistenceUnitName(String.valueOf(Database.MYSQL));

        localContainerEntityManagerFactoryBean.setPersistenceProvider(hibernatePersistenceProvider());
        //设置jpa方言
        localContainerEntityManagerFactoryBean.setJpaDialect(hibernateJpaDialect());
        //配置 JPA 提供商的适配器. 可以通过内部 bean 的方式来配置
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter());
        //配置实体类所在的包
        localContainerEntityManagerFactoryBean.setPackagesToScan(new String[]{StaticConstant.JPA_PACKAGES_TO_SCAN});
        //配置 JPA 的基本属性. 例如 JPA 实现产品的属性
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", hibernate_dialect);
        properties.setProperty("hibernate.show_sql", hibernate_show_sql);
        properties.setProperty("hibernate.format_sql", hibernate_format_sql);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        properties.setProperty(
                "hibernate.current_session_context_class",
                "org.springframework.orm.hibernate4.SpringSessionContext");
        localContainerEntityManagerFactoryBean.setJpaProperties(properties);
        localContainerEntityManagerFactoryBean.afterPropertiesSet();
        localContainerEntityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    /**
     * 配置 JPA 使用的事务管理器
     *
     * @return
     */
    @Bean(name = "jpaTransactionManager")
    @Qualifier(value = "JPA")
    public JpaTransactionManager jpaTransactionManager() {
        LOGGER.debug("PersistenceConfig 中 5、配置 JPA 使用的事务管理器transactionManager……");
        System.out.println("PersistenceConfig 中 5、配置 JPA 使用的事务管理器 transactionManager……");
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setDataSource(druidDataSource);
        jpaTransactionManager.setEntityManagerFactory(jpaEntityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        LOGGER.debug("PersistenceConfig 中 10、配置 数据库初始化……");
        System.out.println("PersistenceConfig 中 10、数据库初始化");
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(druidDataSource);
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource(StaticConstant.DATASOURCE_INIT_CLASS_PATH_RESOURCE));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);
        dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
        return dataSourceInitializer;
    }

}



