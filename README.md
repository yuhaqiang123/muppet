新增ioc部分功能，例如bean存取，constructor实例化依赖启动，aware,capable依赖及服务bean的支持，
打算在框架内部引入ioc，实现模块间的解耦
## muppet（大眼蛙）:是使用java语言开发的，对jdbc进行封装，实现简单对象关系映射的开源持久化框架.
     目的是在灵活性与开发效率（使用简洁性）两者间取得平衡，使开发人员快速的上手数据库编程，又可以针对
     应用进行定制性的再次开发，实现对sql的灵活控制。目前实现CRUD及结果集映射，基于对象生成修改数据表，
     ，数据表自动生成实体类，内部添加了监听器监听容器生命周期，内部aop实现切面拦截，事务控制，
     查询工具类增强单表查询效率。


### muppet 1.5新增功能如下：
  1.事务控制
  2、AOP
  3.数据表自动生成实体类
  4.查询工具类。

#### 事务控制
    数据开发中，事务是很重要的概念，下面使用事务的示例代码。
   SessionFactory factroy = new SessionFactory("muppet.xml");//创建sessionfactory，传入参数是配置文件的路径，实例配置文件见下
   Session session = factroy.getSession(false);//创建Session,false代表关闭自动提交
   Transaction transaction = null;
   try{
         transaction = session.beginTransaction();//开始事务
         ///执行增删查改方法。
         transaction.commit();//提交事务
   }catch(SQLException e){
         if(transaction!=null){
            transaction.rollback();//回滚事务
         }
   }finally{
         session.close();//关闭session
   }


#### 反射及Aop工具包：
      如果你想在你项目中使用动态代理某个类，那么使用我们的工具包，你只要需要实现JDK  InvocationHandle接口既可以，
      与jdk  Proxy类提供的只能对接口代理，使用ReflectUtil类可以透明的对类，接口代理，只需要实现同一个接口InvocationHandle。这个
      接口是jdk自带的，所以当你想法自己实现aop的时候，直接替换掉ReflectUtil类即可，不用更改代理增强类（实现InvocationHandle接口的类）的逻辑。
            对类代理    ReflectUtil.getClassProxy(new A(),new ClosedInvocationHandler());
            对接口代理  ReflectUtil.getProxy(new A(),new ClosedInvocationHandler());
      分别A对象代理，只不过一个是对接口代理，一个对类代理。具体详细使用及原理参考  微信公众平台 IT之路  历史消息
      其他常用反射方法也封装在ReflectUtil类中
  数据表实体类自动生成：
    执行一行代码输入配置文件
        AutoGenerateUtil.generate("muppet.xml");
     配置文件只需要有

     <!--配置数据源-->
     <datasource>
        <url value="jdbc:mysql://localhost:3306/study?Unicode=true"/>
    </datasource>
     <table tableName="tb_bloghouse" domainObjectName="BlogHouse"></table><!--指定哪些表需要自动生成，以及生成的javabean的名字-->
     <generate packageName="cn.bronzeware.test1"></generate><!--在哪个包下生成-->

   Criteria查询工具类
        SessionFactory factroy = new SessionFactory("muppet.xml");//创建sessionfactory，传入参数是配置文件的路径，实例配置文件见下
        Session session = factroy.getSession(false);//创建Session,false代表关闭自动提交
        Criteria criteria = session.createCriteria(Note.class);//创建工具查询
        //criteria.andPropEqual("id", 37);//相等
        //criteria.andPropGreater("id", 36);//大于
        //criteria.andPropGreaterEq("id", 39);//大于等于
        //criteria.andPropGreater("id", 36).order("user_id", false).order("id", false);//排序，链式调用
        //criteria.andPropLess("id", 37);//小于
        criteria.andPropGreater("user_id", 30);//大于
        criteria.andPropLike("username", "%yuhai1%");//like
        criteria.or(criteria1);//or,连接两个查询，做or连接
        System.out.print(criteria.list());//查询结果，返回结果集
###    分表：
      截止目前，还没有实现，原因一是；实现分表需要用加入过滤器链，层层拦截请求，但是添加了过滤器对原有的系统的侵入太大，降低了稳定性，大大增加了难度。原因二：由于分表之后的查询多种多样，join,group by ,order的支持（还在想，到底支持不支持这些操作）等，目前实现起来比较困难。
      目前的想法是先使用本框架开发一个分表应用，但是分表是在框架之上，与之前的设计作比较，哪种实现可行性高，稳定性，效率高，最后在决定使用哪种方式。欢迎新思路，新的小伙伴加入
