package com.rbkmoney.proxy.mocketbank.mpi.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UrlUtils {

    public static String prepareCallbackUrl(String callbackUrl, String path) {
        return UriComponentsBuilder.fromUriString(callbackUrl)
                .path(path)
                .build()
                .toUriString();
    }

    public static String decodeUri(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

}
