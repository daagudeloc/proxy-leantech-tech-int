package com.leantech.proxy.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.core.type.TypeReference;
import com.leantech.proxy.service.ProxyService;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@SpringBootTest
public class ProxyUtilsTest {

    private static final String HEADERS_FILE_PATH = "src/test/resources/files/ExpectedHeadersList.json";

    @Autowired
    private ProxyService proxyService;

    @Test
    public void shouldLogAllHeaders() throws IOException {

        final List<String> expectedHeaders = ProxyUtils.getMapper().readValue(new File(HEADERS_FILE_PATH),
                new TypeReference<>() {});

        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);

        proxyService.getProxiedUrl("http", "google.com");

        final List<ILoggingEvent> logList = listAppender.list;

        assertThat(logList, samePropertyValuesAs(expectedHeaders));

    }

}
