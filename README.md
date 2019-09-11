# <img src="http://ww1.sinaimg.cn/large/007BVBG7gy1g04w3vkdvdj304g02s0sp.jpg" width="120" height="80"> Seed Framework


## Quick Start:

使用和spring容器类似,创建AnnotationConfigApplicationContext加载一个配置类。在配置类上添加注解来扫包。将bean添加到容器当中。
之后就可以在容器中获取bean了。
    
### Example:

```java

 @Configuration
 @ComponentScan("hiyouka.seedframework.context")
 @Import({ImportClass.class,ImportClass2.class})
 @PropertySources("classpath:/test.properties")
 public class ConfigClass {
 
     @Bean("innerBeanClass")
     @Lazy
     @Primary
     public BeanClass beanClass(){
         return new BeanClass();
     }
 
 }
 
 class Test{
     public static void main(String[] args){
         ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigClass.class);
         AnoTest bean = applicationContext.getBean(AnoTest.class);
     }
 }

```

### add Bean:
1. @ComponentScan : 配置扫包路径(将配置路径的@Componment标记的添加到容器当中)

2. @Import: 使用@Import导入指定类(目前支持可实例化的了)

3. @Bean: 使用指定的方法导入类(@Bean导入的对象会被当做配置类继续处理，这点和spring不一样)

### bean lifecycle

bean在创建之后初始化过程：
1. beanPostProcessor 的前置处理方法。

2. 初始化方法执行: 先执行实现Initialization接口的afterPropertiesSet方法，之后执行bean的init方法(@Bean initMethod属性或者在
类中的方法上添加@InitMethod注解)

3. beanPostProcessor 的后置处理方法

4. bean销毁时调用destroy方法(@DestroyMethod或@Bean(destroyMethod)指定销毁方法)

## TODO LIST

* [x] @Autowired 依赖注入功能。
* [ ] 添加seed-aop模块使用cglib实现注解形式支持aop功能，生成代理bean。
* [ ] 添加seed-web模块使用netty实现web功能。

### seed-web模块

    HttpDispatcher 进行任务的调度分发。
    
## 更新日志

### 0.0.2-SNAPSHOT: 
1. 支持使用@Autowired,@Value为容器注入属性。(目前@Value只支持注入有yml/properties文件的环境信息和#{123}表达式的默认属性)
2. 添加InstantiationAwareBeanPostProcessor,提供在bean创建前返回bean和Bean创建后对属性处理的机会。
3. 初始读取资源文件路径下的`seed.properties`和`seed.yml`文件,加载属性进入environment。

#### Example:
```java
@Component
@Priority(100)
@Primary
public class TestBean1<T,D> extends TestFather1<Test2,Test2>{
    @Autowired
    private TestBean2 testBean2;
}

@Component
@Priority(99)
public class TestBean2 extends TestFather1<Test1, Test1> {

    @Value("${spring.aop.auto}")
    private String propertiesKey;
    
    @Value("#{123}")
    private String key;
    
    @Autowired
    @Specify("testBeanOfManual")
    private TestBean1 testBean1;

    @Autowired
    private TestBean2 testBean2;
}

@Component
public class TestAutowired {
    @Autowired
    @Specify("testBean1")
    private TestFather1 testFatherPrimary;
    
    @Autowired
    private TestBean1<String,Object> testBean2;
}

@Configuration
@ComponentScan("hiyouka.framework.test")
public class TestConfiguration {
    @Bean("testBeanOfManual")
    public TestBean1<String,Object> stringObjectTestBean1(){
        return new TestBean1<>();
    }
}
```

### 0.0.3-SNAPSHOT:
1. 支持aop功能
2. 修复@Bean无法创建带有参数的方法的bean(如果容器中有该类型的类则会注入，否则注入null)

## License

The Seed Framework is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).
