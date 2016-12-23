package com.rbkmoney.proxy.mocketbank.mpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.rbkmoney.proxy.mocketbank.mpi"})
public class ProxyMocketBankMpiApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ProxyMocketBankMpiApplication.class, args);
    }
}
