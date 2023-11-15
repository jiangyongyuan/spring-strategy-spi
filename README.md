

# Strategy API in Spring system

### 介绍

策略模式是一种行为设计模式，在运行时根据参数选择对应的算法。
程序并不是实现一个算法，而是在运行时根据参数在一组程序中选择对应的算法。
策略模式在复杂系统中非常常见。

市场上有不少插件系统，使用较为麻烦，Spring-strategy-spi使用比较简单的方式，通过SPI<Class>.strategy即可在Spring项目中根据名称获取策略类。

关于SPI的命名，是希望在系统设计时，能从整个系统的视角，定义项目中的扩展点，从而支持复杂系统的构建。

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
也不用再为ITest去定义对应的ITestFactory，使用SPI<ITest>即可通过对应的name进行获取。

### 实现原理

定义了一个SPI代理接口，在属性解析到对应的参数时，生成代理类。在项目启动完成后，通过BeanFactory获取全部的实现，读取对应的SPIName定义的key，设置default的实现。
即完成了Spring中list bean到map bean 的一个实现。具体的话看下源码实现，整个实现非常简单。

### 使用

[Maven central](https://mvnrepository.com/artifact/io.github.jiangyongyuan/spring-strategy-spi/1.0)

```xml
<dependency>
    <groupId>io.github.jiangyongyuan</groupId>
    <artifactId>spring-strategy-spi</artifactId>
    <version>1.0</version>
</dependency>
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
