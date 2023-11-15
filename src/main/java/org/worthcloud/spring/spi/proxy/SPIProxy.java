package org.worthcloud.spring.spi.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.worthcloud.spring.spi.SPI;
import org.worthcloud.spring.spi.SPIName;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SPIProxy<T> implements SPI {

    T strategyClass ;

    ConfigurableListableBeanFactory beanFactory;

    ConcurrentHashMap<Object,Object> beans = new ConcurrentHashMap<>();

    volatile boolean isReady = false;

    //默认的key实现
    public static final String DEFAULT_STRATEGY_KEY = "DEFAULT";

    public SPIProxy(T argType ) {
        //target class
        strategyClass = argType;
    }

    public void setBeanFactory( ConfigurableListableBeanFactory beanFactory ){
        this.beanFactory = beanFactory;
    }

    public void init(){
        if( !isReady ) {
            Map<String, T> beansOfType = (Map<String, T>) beanFactory.getBeansOfType((Class) strategyClass);

            for (Map.Entry e : beansOfType.entrySet()) {
                T v = (T) e.getValue();
                findStrategyBean(v);
            }
            isReady = true;
        }
    }


    /**
     * 找到@SPIName 并注入
     * @param bean
     */
    private void findStrategyBean(Object bean ){
        Object key = DEFAULT_STRATEGY_KEY ;
        try {
            SPIName annotation = AnnotationUtils.findAnnotation(bean.getClass(), SPIName.class);

            if( annotation != null ) {
                key = annotation.value();
            }

            if (beans.containsKey(key)) {
                log.warn("[WorthCloud][SPI] !!!duplicate strategy name : {} , bean :{}", key, bean);
//                throw new RuntimeException("[SPIProxy] duplicate strategy name : " + bean);
            }
            log.info("[WorthCloud][SPI] {},{}={}" , strategyClass , key , bean );
            beans.put(key, bean);
        }catch (Exception e ){
            log.error( "[WorthCloud][SPI] Class {} inject error,key = {} " , bean.getClass()  , key , e );
        }
    }


    @Override
    public Object strategy(Object strategy) {
        if( !isReady ){
            log.warn(" SPI<{}> not ready , maybe could not found the strategy bean if circular." , strategyClass );

            return beans.get( strategy );
        }

        Object bean = beans.get( strategy );

        return bean != null ? bean : beans.get( DEFAULT_STRATEGY_KEY );
    }
}
