

# Strategy API in Spring system

[Maven central](https://mvnrepository.com/artifact/io.github.jiangyongyuan/spring-strategy-spi/1.0)]

### 介绍

策略模式是一种行为设计模式，在运行时根据参数选择对应的算法。
程序并不是实现一个算法，而是在运行时根据参数在一组程序中选择对应的算法。
策略模式在复杂系统中非常常见。

尽管策略模式非常常见，但是在项目中却有非常多不同的代码实现：不同的工程师、不同的策略类可能就定义一套自己的BeanFactory。一个工程里面可能有很多个XXXBeanFactory。

另外由于Dubbo的SPI机制非常出名，但是不少项目中在项目已经使用Spring的基础上，自定义一套META-INF目录，自己去做文件解析，bean注册、注入，整个设计非常复杂并且不好维护。
个人认为，由于Dubbo并不是基于Spring构建，所以自行设计了对应的文件解析是可以理解，但是对于Spring构建的Bean，就不用自己再去做注入了，通过Spring即可进行不同文件、类的加载。

我们需要的SPI机制，可能更多的是策略模式。比如ExtensionLoader中的Wrapper模式，可能在Spring中通过AOP的模式即可实现。对于SPI的理解，我们更多的应该从构建可扩展系统去了解。
即提供默认实现的策略，也支持通过不同的上下文、参数，使用对应的策略。

因此，项目名称我们借用SPI的方式，希望在系统设计时，能从整个系统的视角，定义项目中的扩展点，从而支持复杂系统的构建。


### 核心技术

Spring-strategy-spi 希望提供一种标准、简单、通用的方法，通过key去获取已经注册的服务，而无需自己去将key、bean put到自定义的一个Factory中。
Spring-strategy-spi 提供 `SPI<Class>` 代理类，该代理类会加载`<Class>`定义的多个策略实现。
每个策略类的key，通过注解`@SPIName(key)`定义，以下是代码示例：

```java
@SpringBootApplication
public class TestApplication  {

    @Resource
    SPI<ITest> test;

    public void test(){
        test.strategy("A").helloWorld();
        test.strategy("B").helloWorld();
    }

    public static void main(String[] args) {
        SpringApplication.run( TestApplication.class , args);
    }

}

@Service
@SPIName("A")
public class ITestA implements ITest{
    @Override
    public void helloWorld() {
        log.info("ITestA helloWorld ! ");
    }
}

@Service
@SPIName("B")
public class ITestB implements ITest{
    @Override
    public void helloWorld() {
        log.info("ITestB helloWorld ! ");
    }
}

```

ITest的实现不用去定义策略name的接口，因为加的策略name可阅读性并没有`SPIName`注解来得好。
不用再为ITest去定义对应的ITestFactory，使用SPI<ITest>即可通过对应的name进行获取。

### 实现原理

定义了一个SPI代理接口，在属性解析到对应的参数时，生成代理类。在项目启动完成后，通过BeanFactory获取全部的实现，读取对应的SPIName定义的key，设置default的实现。
即完成了Spring中list bean到map bean 的一个实现。具体的话看下源码实现，整个实现非常简单。

### 使用

```xml
<dependency>
    <groupId>io.github.jiangyongyuan</groupId>
    <artifactId>spring-strategy-spi</artifactId>
    <version>1.0</version>
</dependency>
```

### SPI Wrapper 的思路

如果需要dubbo spi的Wrapper的机制，可以通过aop的方式定义，往往项目里面可能不需要很多层的嵌套，没必要进行一个可嵌套的Wrapper设计。

以下是Spring aop的一个demo参考：

```java
@Slf4j
@Aspect
@Component
public class SPIWrapper1 implements Ordered {

    @Pointcut(value = "execution(* com.worthcloud.demo.spi.SPITest.test(..))" )
    public void test(){
    }

    @Before(value = "com.worthcloud.demo.spi.SPIWrapper1.test() && args(name)" )
    public void before( String name  ) throws Throwable{
        log.info("SPIWrapper 1 invoke : test ");
    }

    @AfterReturning(value = "com.worthcloud.demo.spi.SPIWrapper1.test()" ,returning = "retVal")
    public void after( String retVal ) throws Throwable{
        log.info("retVal 1 = " + retVal );
    }


    @Override
    public int getOrder() {
        return 2;
    }
}
```


###


# The strategy api in Spring system

## Preface

### Introduction

The strategy pattern is a behavioral software design pattern that enables selecting an algorithm at runtime.
Instead of implementing a single algorithm directly, code receives run-time instructions as to which in a family of algorithms to use.
[https://en.wikipedia.org/wiki/Strategy_pattern]. The strategy pattern is common in many complex applications.

Spring-Strategy-Spi provides a more pragmatic approach to obtain a strategy bean via key . Which project has multiple implementations build with Spring .

### Technologies

#### Introduction

Spring-Strategy-Spi provide `SPI<Class>` proxy bean . which hold the multiple implementations of the `<Class>` interface.
The multiple strategy bean's key define by the `@SPIName` annotation , here is the example :

```java
@SpringBootApplication
public class TestApplication  {

    @Resource
    SPI<ITest> test;

    public void test(){
        test.strategy("A").helloWorld();
        test.strategy("B").helloWorld();
    }

    public static void main(String[] args) {
        SpringApplication.run( TestApplication.class , args);
    }

}

@Service
@SPIName("A")
public class ITestA implements ITest{
    @Override
    public void helloWorld() {
        log.info("ITestA helloWorld ! ");
    }
}

@Service
@SPIName("B")
public class ITestB implements ITest{
    @Override
    public void helloWorld() {
        log.info("ITestB helloWorld ! ");
    }
}

```

Simply use the spring bean with the `@SPIName` annotation ,it is very convenient to obtain the beans that have been registered in the system through the key.
You don't need to define any more classes, any BeanPostProcessor to register a Map form the BeanFactory.

### Usage

```xml
<dependency>
    <groupId>io.github.jiangyongyuan</groupId>
    <artifactId>spring-strategy-spi</artifactId>
    <version>1.0</version>
</dependency>
```
