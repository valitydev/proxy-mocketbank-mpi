package com.rbkmoney.proxy.mocketbank.mpi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtils {

    public static String prepareCallbackUrl(String callbackUrl, String path) {
        return UriComponentsBuilder.fromUriString(callbackUrl)
                .path(path)
                .build()
                .toUriString();
    }

}
