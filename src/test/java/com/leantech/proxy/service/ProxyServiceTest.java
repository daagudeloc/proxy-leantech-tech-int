package com.leantech.proxy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leantech.proxy.util.ProxyUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProxyServiceTest {

    private static final String STATUSES_PATH = "src/test/resources/files/";

    @Autowired
    private ProxyService proxyService;

    @Test
    public void shouldRedirectSuccessfullyWhen2xxStauses() throws IOException {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-2xx.json", STATUSES_PATH)),
                new TypeReference<>() {});

        httpStatuses.forEach(
                httpStatus -> {
                    final String host = format("%s%s%s", baseHost, httpStatus, sleepTime);
                    final ModelAndView expected = new ModelAndView(format("redirect:http://%s", baseHost));
                    final ModelAndView actual = proxyService.getProxiedUrl(protocol, baseHost);

                    assertThat(actual, samePropertyValuesAs(expected));
                }
        );
    }

    @Test
    public void shouldRedirectWhen3xxStatuses() throws IOException {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-3xx.json", STATUSES_PATH)),
                new TypeReference<>() {
                });

        httpStatuses.forEach(
                httpStatus -> {
                    final String host = format("%s%s%s", baseHost, httpStatus, sleepTime);
                    final ModelAndView expected = new ModelAndView(format("redirect:http://%s", baseHost));
                    final ModelAndView actual = proxyService.getProxiedUrl(protocol, baseHost);

                    assertThat(actual, samePropertyValuesAs(expected));
                }
        );
    }

    @Test
    public void shouldThrowExceptionWhen4xxStatuses() throws IOException {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-4xx.json", STATUSES_PATH)),
                new TypeReference<>() {
                });

        httpStatuses.forEach(
                httpStatus -> {
                    final String host = format("%s%s%s", baseHost, httpStatus, sleepTime);
                    final ModelAndView actual = proxyService.getProxiedUrl(protocol, baseHost);

                    assertThrows(HttpClientErrorException.class, () -> { proxyService.getProxiedUrl(protocol, host); });
                }
        );
    }

    @Test
    public void shouldThrowExceptionWhen5xxStatuses() throws IOException {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-5xx.json", STATUSES_PATH)),
                new TypeReference<>() {
                });

        httpStatuses.forEach(
                httpStatus -> {
                    final String host = format("%s%s%s", baseHost, httpStatus, sleepTime);
                    final ModelAndView actual = proxyService.getProxiedUrl(protocol, baseHost);

                    assertThrows(HttpServerErrorException.class, () -> { proxyService.getProxiedUrl(protocol, host); });
                }
        );
    }

}
