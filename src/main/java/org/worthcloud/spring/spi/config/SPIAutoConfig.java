package org.worthcloud.spring.spi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.worthcloud.spring.spi.proxy.SPIProxyBeanPostProcessor;

@Configuration
@Import(SPIProxyBeanPostProcessor.class)
public class SPIAutoConfig {

}
