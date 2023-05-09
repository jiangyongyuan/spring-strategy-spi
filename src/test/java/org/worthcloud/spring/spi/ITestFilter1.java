//package org.worthcloud.spring.spi;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component
//public class ITestFilter1 {
//
//    @Pointcut("execution(* org.worthcloud.spring.spi.ITest.helloWorld(..))")
//    public void helloWorld() {
//    }
//
//    @Order(1)
//    @Before(value = "helloWorld()" , argNames = "" )
//    public void invoke(  ) throws Throwable{
//        log.info("SPIWrapper 1 invoke : printSpi ");
//        log.info("target = " + target );
//        target.printSpi();
//    }
//
//}
