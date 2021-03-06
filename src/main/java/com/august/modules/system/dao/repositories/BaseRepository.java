package com.august.modules.system.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.modules.system.dao.mapper
 * Author: Administrator
 * Update: Administrator(2015/9/18)
 * Description:基本的JPA管理工具类
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    /**
     * 一、通过前面的配置可以看出，Spring 对 JPA 的支持已经非常强大，开发者无需过多关注 EntityManager 的创建、事务处理等 JPA 相关的处理
     *      ***********************************************************************
     *      然而spring对Jpa的支持不止于此，它要从根本上来简化我们的业务代码
     *      在没用使用jpa支持的时候，我们的代码应该是这样的：
     *      1、*Dao   持久层接口
     *      2、*DaoImpl   持久层实现类
     *      3、*Service    业务层接口.....后面不在列举
     *      每写一个实体类，都要衍生出5、6个类来对他进行操作，即使有了注解，我们可以依赖注入方式来拿到实现类，
     *      但是通用的CRUD等操作却不免在每个实现类里声明，你又说，我可以定义一个父类，利用泛型反射原理就可以了，
     *      但那样你还需要为每个Dao声明自己的实现类来继承你的父类
     *      ***********************************************************************
     *      那么问题来了...（不是挖掘机技术）对持久层的简化技术哪家强？      Spring Data Jpa
     *      你唯一要做的就只是声明持久层的接口，来继承他自身已经封装好的持久层接口，正如本类*Dao一样
     *      可使用的接口有：
     *          Repository：  是 Spring Data的一个核心接口，它不提供任何方法，目的是为了统一所有Repository的类型，且能让组件扫描的时候自动识别，开发者需要在自己定义的接口中声明需要的方法。
     *          CrudRepository：  继承Repository，提供增删改查方法，可以直接调用。
     *          PagingAndSortingRepository：  继承CrudRepository，具有分页查询和排序功能（本类实例）
     *          JpaRepository：   继承PagingAndSortingRepository，针对JPA技术提供的接口,增加了一些实用的功能，比如：批量操作等
     *          JpaSpecificationExecutor：          可以执行原生SQL查询,用来做负责查询的接口
     *          Specification：是Spring Data JPA提供的一个查询规范，要做复杂的查询，只需围绕这个规范来设置查询条件即可
     *      继承不同的接口，有两个不同的泛型参数，他们是该持久层操作的类对象和主键类型。
     *      ********************************************************************************
     *
     * 二、JpaRepository基本功能
     *      JpaRepository所有可用方法
     * 三、JpaRepository的查询
     *      直接在接口中定义查询方法，如果是符合规范的，可以不用写实现，目前支持的前缀如下：
     *      find、findBy、read、readBy、get、getBy
     *      目前支持的关键字写法如下：
     *      keyWord             simple                                                  JPQLSnippet
     *      And                 findByUsernameAndPassword(String user, String pwd);     where x.user=?1 and x.pws=?2
     *      Or                  findByUsernameOrAddress(String user, String address);   where x.user=?1 or x.address=?2
     *      Between             findBySalaryBetween(int max, int min);                  where x.salary between ?1 and ?2
     *      LessThan            findBySalaryLessThan(int max);                          where x.salary < ?1
     *      GreaterThan         findBySalaryGreaterThan(int min);                       where x.salary > ?1
     *      After               findBySalaryAfter(int min)                              where x.salary > ?1
     *      Before              findBySalaryBefore(int max)                             where x.salary < ?1
     *      IsNull              findByUsernameIsNull();                                 where x.username is null
     *      IsNotNull,NotNull   findByUsernameIsNotNull();                              where x.username not null
     *      Like                findByUsernameLike(String user);                        where x.username like ?1
     *      NotLike             findByUsernameNotLike(String user);                     where x.username not like ?1
     *      StartingWith        findByUsernameStartingWith(String user);                where x.username like ?1(parameter bound with appended %)//后缀
     *      EndingWith          findByUsernameEndingWith(String user);                  where x.username like ?1(parameter bound with prepended %)//前缀
     *      Containing          findByUsernameContaining(String user);                  where x.username like ?1(parameter bound wrapped in %)//包含
     *      OrderBy             findByUsernameOrderBySalaryAsc(String user);            where x.username = ?1 order by salaryAsc
     *      Not                 findByUsernameNot(String user);                         where x.username <> ?1
     *      In                  findByUsernameIn(Collection<String> userList) ，方法的参数可以是 Collection 类型，也可以是数组或者不定长参数;
     *                                                                                  where x.username in ?1
     *      NotIn               findByUsernameNotIn(Collection<String> userList) ，方法的参数可以是 Collection 类型，也可以是数组或者不定长参数;
     *                                                                                  where x.username not in ?1
     *      True                findByActiveTrue()                                      where active=true
     *      False               findByActiveFalse()                                     where active=false
     *      可能会存在一种特殊情况，比如 Doc包含一个 user 的属性，也有一个 userDep 属性，此时会存在混淆。
     *      可以明确在属性之间加上 "_" 以显式表达意图，比如 "findByUser_DepUuid()" 或者 "findByUserDep_uuid()"
     *      特殊的参数： 还可以直接在方法的参数上加入分页或排序的参数，比如：
     *      Page<UserModel> findByName(String name, Pageable pageable);
     *      List<UserModel> findByName(String name, Sort sort);
     *      也可以使用JPA的NamedQueries，方法如下：
     *      1：在实体类上使用@NamedQuery，示例如下：
     *      @NamedQuery(name = "UserModel.findByAge",query = "select o from UserModel o where o.age >= ?1")
     *      2：在自己实现的DAO的Repository接口里面定义一个同名的方法，示例如下：
     *      public List<UserModel> findByAge(int age);
     *      3：然后就可以使用了，Spring会先找是否有同名的NamedQuery，如果有，那么就不会按照接口定义的方法来解析。
     *
     *      使用@Query
     *      可以在自定义的查询方法上使用@Query来指定该方法要执行的查询语句，比如：
     *      @Query("select o from UserModel o where o.uuid=?1")
     *      public List<UserModel> findByUuidOrAge(int uuid);
     *      注意：
     *      1：方法的参数个数必须和@Query里面需要的参数个数一致
     *      2：如果是like，后面的参数需要前面或者后面加“%”，比如下面都对：
     *      @Query("select o from UserModel o where o.name like ?1%")
     *      public List<UserModel> findByUuidOrAge(String name);
     *
     *      @Query("select o from UserModel o where o.name like %?1")
     *      public List<UserModel> findByUuidOrAge(String name);
     *
     *      @Query("select o from UserModel o where o.name like %?1%")
     *      public List<UserModel> findByUuidOrAge(String name);
     *
     *      当然，这样在传递参数值的时候就可以不加‘%’了，当然加了也不会错
     *
     *      还可以使用@Query来指定本地查询，只要设置nativeQuery为true，比如：
     *      @Query(value="select * from tbl_user where name like %?1" ,nativeQuery=true)
     *      public List<UserModel> findByUuidOrAge(String name);
     *      注意：当前版本的本地查询不支持翻页和动态的排序
     *
     *      使用命名化参数，使用@Param即可，比如：
     *      @Query(value="select o from UserModel o where o.name like %:nn")
     *      public List<UserModel> findByUuidOrAge(@Param("nn") String name);
     *      同样支持更新类的Query语句，添加@Modifying即可，比如：
     *      @Modifying
     *      @Query(value="update UserModel o set o.name=:newName where o.name like %:nn")
     *      public int findByUuidOrAge(@Param("nn") String name,@Param("newName") String newName);
     *      注意：
     *      1：方法的返回值应该是int，表示更新语句所影响的行数
     *      2：在调用的地方必须加事务，没有事务不能正常执行
     *
     *      JpaRepository的查询功能
     *      创建查询的顺序
     *      Spring Data JPA 在为接口创建代理对象时，如果发现同时存在多种上述情况可用，它该优先采用哪种策略呢？
     *      <jpa:repositories> 提供了 query-lookup-strategy 属性，用以指定查找的顺序。它有如下三个取值：
     *      1：create-if-not-found：如果方法通过@Query指定了查询语句，则使用该语句实现查询；
     *      如果没有，则查找是否定义了符合条件的命名查询，如果找到，则使用该命名查询；
     *      如果两者都没有找到，则通过解析方法名字来创建查询。这是 query-lookup-strategy 属性的默认值
     *      2：create：通过解析方法名字来创建查询。即使有符合的命名查询，或者方法通过 @Query指定的查询语句，都将会被忽略
     *      3：use-declared-query：如果方法通过@Query指定了查询语句，则使用该语句实现查询；如果没有，则查找是否定
     *      义了符合条件的命名查询，如果找到，则使用该命名查询；如果两者都没有找到，则抛出异常
     *
     *四、客户化扩展JpaRepository
     *      如果你不想暴露那么多的方法，可以自己订制自己的Repository，还可以在自己的Repository里面添加自己使用的公共方法
     *      当然更灵活的是自己写一个实现类，来实现自己需要的方法
     *      1：写一个与接口同名的类，加上后缀为Impl，这个在前面xml里面配置过，可以自动被扫描到。这个类不需要实现任何接口。
     *      2：在接口中加入自己需要的方法，比如：
     *        public Page<Object[]> getByCondition(UserQueryModel u);
     *      3：在实现类中，去实现这个方法就好了，会被自动找到
     *
     *五、Specifications查询
     *     Spring Data JPA支持JPA2.0的Criteria查询，相应的接口是JpaSpecificationExecutor。
     *     Criteria 查询：是一种类型安全和更面向对象的查询
     *     这个接口基本是围绕着Specification接口来定义的， Specification接口中只定义了如下一个方法：
     *     Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
     *
     *     要理解这个方法，以及正确的使用它，就需要对JPA2.0的Criteria查询有一个足够的熟悉和理解，因为这个方法的参数和返回值都是JPA标准里面定义的对象。
     *
     *     Criteria查询基本概念
     *     Criteria 查询是以元模型的概念为基础的，元模型是为具体持久化单元的受管实体定义的，这些实体可以是实体类，嵌入类或者映射的父类。
     *     CriteriaQuery接口：代表一个specific的顶层查询对象，它包含着查询的各个部分，比如：select 、from、where、group by、order by等
     *     注意：CriteriaQuery对象只对实体类型或嵌入式类型的Criteria查询起作用
     *     Root接口：代表Criteria查询的根对象，Criteria查询的查询根定义了实体类型，能为将来导航获得想要的结果，它与SQL查询中的FROM子句类似
     *       1：Root实例是类型化的，且定义了查询的FROM子句中能够出现的类型。
     *       2：查询根实例能通过传入一个实体类型给 AbstractQuery.from方法获得。
     *       3：Criteria查询，可以有多个查询根。
     *       4：AbstractQuery是CriteriaQuery 接口的父类，它提供得到查询根的方法。
     *     CriteriaBuilder接口：用来构建CriteriaQuery的构建器对象
     *     Predicate：一个简单或复杂的谓词类型，其实就相当于条件或者是条件组合。
     *
     *     Criteria查询
     *     基本对象的构建
     *     1：通过EntityManager的getCriteriaBuilder或EntityManagerFactory的getCriteriaBuilder方法可以得到CriteriaBuilder对象
     *     2：通过调用CriteriaBuilder的createQuery或createTupleQuery方法可以获得CriteriaQuery的实例
     *     3：通过调用CriteriaQuery的from方法可以获得Root实例
     *
     *     过滤条件
     *     1：过滤条件会被应用到SQL语句的FROM子句中。在criteria 查询中，查询条件通过Predicate或Expression实例应用到CriteriaQuery对象上。
     *     2：这些条件使用 CriteriaQuery .where 方法应用到CriteriaQuery 对象上
     *     3：CriteriaBuilder也作为Predicate实例的工厂，通过调用CriteriaBuilder 的条件方法（ equal，notEqual， gt， ge，lt， le，between，like等）创建Predicate对象。
     *     4：复合的Predicate 语句可以使用CriteriaBuilder的and, or andNot 方法构建。
     *
     *     构建简单的Predicate示例：
     *     Predicate p1=cb.like(root.get(“name”).as(String.class), “%”+uqm.getName()+“%”);
     *     Predicate p2=cb.equal(root.get("uuid").as(Integer.class), uqm.getUuid());
     *     Predicate p3=cb.gt(root.get("age").as(Integer.class), uqm.getAge());
     *     构建组合的Predicate示例：
     *     Predicate p = cb.and(p3,cb.or(p1,p2));
     *
     *     当然也可以形如前面动态拼接查询语句的方式，比如：
     *     Specification<UserModel> spec = new Specification<UserModel>() {
     *       public Predicate toPredicate(Root<UserModel> root,  CriteriaQuery<?> query, CriteriaBuilder cb) {
     *         List<Predicate> list = new ArrayList<Predicate>();
     *         if(um.getName()!=null && um.getName().trim().length()>0){
     *           list.add(cb.like(root.get("name").as(String.class), "%"+um.getName()+"%"));
     *         }
     *         if(um.getUuid()>0){
     *           list.add(cb.equal(root.get("uuid").as(Integer.class), um.getUuid()));
     *         }
     *         Predicate[] p = new Predicate[list.size()];
     *         return cb.and(list.toArray(p));
     *      }
     *     };
     *
     *
     */
}
