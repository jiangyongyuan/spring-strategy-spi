package org.worthcloud.spring.spi;

import org.junit.Assert;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;


@SpringBootApplication
public class TestApplication  {

    @Resource
    SPI<ITest> test;

    @EventListener(ApplicationReadyEvent.class)
    public void test(){
        test.strategy("A").helloWorld();
        test.strategy("B").helloWorld();
        test.strategy(3 ).helloWorld();

        Assert.assertNotNull( test.strategy( "A"));
    }

    public static void main(String[] args) {
        SpringApplication.run( TestApplication.class , args);
    }

}