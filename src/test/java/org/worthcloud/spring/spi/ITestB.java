package org.worthcloud.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@SPIName("B")
public class ITestB implements ITest{
    @Override
    public void helloWorld() {
        log.info("ITestB helloWorld ! ");
    }
}
