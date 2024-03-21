package org.worthcloud.spring.spi;

import org.worthcloud.spring.spi.proxy.SPIProxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SPIKey {

    //默认的key:没有定义key就是默认实现
    String value() default SPIProxy.DEFAULT_STRATEGY_KEY;

}
