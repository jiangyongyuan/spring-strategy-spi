package org.worthcloud.spring.spi.sort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.worthcloud.spring.spi.SPI;

import javax.annotation.Resource;


@SpringBootApplication
public class SortApplication {

    @Resource
    SPI<Sort> sort;

    @EventListener(ApplicationReadyEvent.class)
    public void test(){
        int[] arr = {7, 2, 1, 6, 8, 5, 3, 4};
        sort.strategy("Quick").sort( arr );
    }

    public static void main(String[] args) {
        SpringApplication.run( SortApplication.class , args);
    }

}