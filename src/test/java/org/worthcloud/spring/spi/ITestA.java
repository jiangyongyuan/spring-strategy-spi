package org.worthcloud.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SPIKey
public class ITestA implements ITest{

    @Override
    public void helloWorld() {
        log.info("ITestA helloWorld ! ");
    }

    @Override
    public void testArgs(String name) {
        log.info("ItestA testArgs ");
    }

}
