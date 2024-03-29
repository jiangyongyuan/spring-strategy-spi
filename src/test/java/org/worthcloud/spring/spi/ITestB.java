package org.worthcloud.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SPIKey("B")
public class ITestB implements ITest{

    @Override
    public void helloWorld() {
        log.info("ITestB helloWorld ! ");
    }

    @Override
    public void testArgs(String name) {
        log.info("ItestB testArgs ");
    }

}
