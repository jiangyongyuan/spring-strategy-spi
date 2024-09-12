package org.worthcloud.spring.spi.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.worthcloud.spring.spi.SPI;
import org.worthcloud.spring.spi.SPIKey;

import java.util.Arrays;
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
        Object [] keys = null;

        try {
            SPIKey annotation = AnnotationUtils.findAnnotation(bean.getClass(), SPIKey.class);

            if( annotation != null ) {
                key = annotation.value();
                keys = annotation.values();
            }

            if(keys != null && keys.length == 0 ){
                if (beans.containsKey(key)) {
                    log.warn("[SPI] !!!duplicate strategy name : {} , bean :{}", key, bean);
                    throw new RuntimeException("[SPI] duplicate strategy name : " +bean.getClass().getSimpleName()+ " is "+ key);
                }
                log.info("[SPI] {},{}={}" , strategyClass , key , bean );
                beans.put(key, bean);
            }
            if(keys != null && keys.length > 0 ){
                //multiple key(strategy) use same bean
                Arrays.stream(keys).forEach(k->beans.put(k,bean));
            }
        }catch (Exception e ){
            log.error( "[SPI] Class {} inject error,key = {} " , bean.getClass()  , key , e );
        }
    }

    /**
     * 获取策略类
     * @param strategy the strategy key , it will force to String
     * @return
     */
    @Override
    public Object strategy(Object strategy) {
        String spiKey = strategy instanceof String ? (String)strategy : strategy + "";

        if( !isReady ){
            log.warn(" SPI<{}> not ready , maybe could not found the strategy bean if circular." , strategyClass );

            return beans.get( spiKey );
        }

        Object bean = beans.get( spiKey );

        return bean != null ? bean : beans.get( DEFAULT_STRATEGY_KEY );
    }
}
