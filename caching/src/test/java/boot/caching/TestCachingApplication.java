package boot.caching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestCachingApplication {

    public static void main(String[] args) {
        SpringApplication.from(CachingApplication::main)
                .with(TestCachingApplication.class)
                .with(TestContainerConfig.class)
                .run(args);
    }

}
