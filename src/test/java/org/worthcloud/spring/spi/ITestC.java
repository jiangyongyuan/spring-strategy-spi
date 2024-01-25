package org.worthcloud.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SPIName("C")
public class ITestC implements ITest{

    @Override
    public void helloWorld() {
        log.info("ITestC helloWorld ! ");
    }

    @Override
    public void testArgs(String name) {
        log.info("ItestC testArgs ");
    }

}
