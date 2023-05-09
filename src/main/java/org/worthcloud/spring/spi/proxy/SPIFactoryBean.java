package org.worthcloud.spring.spi.proxy;


import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

import java.lang.reflect.Proxy;

@Slf4j
public class SPIFactoryBean<T> implements FactoryBean<T> , BeanFactoryAware  , SmartApplicationListener {

    private Class<T> interfaceClass;

    private Class argType;

    private SPIProxy proxy ;


    public SPIFactoryBean( Class<T> interfaceClass , Class argType ){
        this.interfaceClass = interfaceClass;
        this.argType = argType;

        proxy = new SPIProxy( argType );
    }


    @Override
    public T getObject() throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget( proxy );
        proxyFactory.addInterface( interfaceClass );

        return (T)proxyFactory.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        proxy.setBeanFactory( (ConfigurableListableBeanFactory) beanFactory );
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
//        log.info( "SPIFactoryBean : spring event : {} . tips: junit test will mock the applicationEvent . " , eventType );
        return ApplicationReadyEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        proxy.init();
    }
}
