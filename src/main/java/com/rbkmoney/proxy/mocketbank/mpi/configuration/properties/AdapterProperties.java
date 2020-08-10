package com.rbkmoney.proxy.mocketbank.mpi.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.commons.nullanalysis.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties("adapter")
public class AdapterProperties {

    @NotNull
    private String callbackUrl;

    @NotNull
    private String pathAcsUrl;
}
