package com.rbkmoney.proxy.mocketbank.mpi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class ProxyMocketBankMpiApplication extends SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProxyMocketBankMpiApplication.class, args);
    }
}
