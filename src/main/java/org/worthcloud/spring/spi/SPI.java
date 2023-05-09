package org.worthcloud.spring.spi;


/**
 * 根据strategy获取bean:
 * 使用方法：
 *
 * 在需要多个策略的服务中，添加 @StrategySPI(key="keyA") 定义该策略的key
 *
 * 通过@Resource注入代理服务，类型为需要策略的服务:
 *
 * @param <T> 接口的名称
 */
public interface SPI<T> {

    /**
     * 获取策略实现
     * @param key the strategy key
     * @return the spring bean
     */
    public T strategy( Object key );

}
