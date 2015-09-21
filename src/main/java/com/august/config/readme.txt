该文件用于放置系统启动的相关配置信息
一、框架搭建过程中遇到的问题以及解决办法：
1、
存在问题：使用myBatis管理数据源时，配置了MapperScannerConfigurer，导致数据源找不到
解决方案：经查得知，如果使用了JavaConfig的方式启动Spring，那么即使是ImportResource了XML文件
        也是无法让MapperScannerConfigurer扫描到的，必须改为同样的JavaConfig方式,即如下方式
        @MapperScan(basePackages = {StaticConstant.MYBATIS_BASE_PACKAGES})
        //设置mapper扫描基本路径，自动引入实体类

2、多数据源问题，
存在问题：多事务不能并存
解决方案：配置如下内容，并指定对应的事务管理器和实体类管理工厂
//配置jpa扫描基本实现类,即数据库操作层相关实现
@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManager",
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        basePackages = StaticConstant.JPA_BASE_PACKAGES)
3、多数据源切换问题；
存在问题：
解决方案：
二、当前框架存在的问题
1、返回信息中文存在乱码