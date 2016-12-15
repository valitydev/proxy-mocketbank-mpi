package com.rbkmoney.proxy.test.mpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.rbkmoney.proxy.test.mpi"})
public class ProxyTestMpiApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProxyTestMpiApplication.class, args);
    }
}
