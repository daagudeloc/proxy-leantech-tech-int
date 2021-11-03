package com.leantech.proxy.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leantech.proxy.service.ProxyService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import static java.lang.String.format;

public class ProxyUtils {

    @Getter
    private static final Logger logger;
    @Getter
    private static final ObjectMapper mapper;

    static {
        logger = LoggerFactory.getLogger(ProxyService.class);
        mapper = new ObjectMapper();
    }

    public static void logHeaders(final HttpHeaders headers) {
        logger.warn("|--------------- HEADERS ---------------|");
        headers.forEach( (key, value) -> logger.warn(format("%s = %s", key, value)) );
        logger.warn("|---------------------------------------|");
    }

}
