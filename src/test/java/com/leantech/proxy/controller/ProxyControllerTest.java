package com.leantech.proxy.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leantech.proxy.util.ProxyUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class ProxyControllerTest {

    private static final String STATUSES_PATH = "src/test/resources/files/";

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeAll
    void setUpMock() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldRedirectWhen2xxStatuses() throws Exception {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-2xx.json", STATUSES_PATH)),
                new TypeReference<>() {});

        httpStatuses.forEach( httpStatus -> testRedirection(baseHost, httpStatus, sleepTime, protocol) );

    }

    @Test
    public void shouldRedirectWhen3xxStatuses() throws Exception {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-3xx.json", STATUSES_PATH)),
                new TypeReference<>() {});

        httpStatuses.forEach( httpStatus -> testRedirection(baseHost, httpStatus, sleepTime, protocol) );

    }

    @Test
    public void shouldThrowExceptionWhen4xxStatuses() throws Exception {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-4xx.json", STATUSES_PATH)),
                new TypeReference<>() {});

        httpStatuses.forEach( httpStatus -> testErrorRequest(baseHost, httpStatus, sleepTime, protocol, status().is4xxClientError()) );

    }

    @Test
    public void shouldThrowExceptionWhen5xxStatuses() throws Exception {
        final String protocol = "http";
        final String baseHost = "httpstat.us/";
        final String sleepTime = "?sleep=0";

        final List<String> httpStatuses = ProxyUtils.getMapper().readValue(new File(format("%s/HttpStatusesToTest-5xx.json", STATUSES_PATH)),
                new TypeReference<>() {});

        httpStatuses.forEach( httpStatus -> testErrorRequest(baseHost, httpStatus, sleepTime, protocol, status().is5xxServerError()) );

    }

    private void testRedirection(final String baseHost, final String httpStatus, final String sleepTime,
                                 final String protocol) {
        final String host = format("%s%s%s", baseHost, httpStatus, sleepTime);

        try {
            mockMvc.perform(get(format("/proxy/%s?url=%s", protocol, host)))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(format("http://%s", host)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testErrorRequest(final String baseHost, final String httpStatus, final String sleepTime,
                                  final String protocol, final ResultMatcher matcher) {
        final String host = format("%s%s%s", baseHost, httpStatus, sleepTime);

        try {
            mockMvc.perform(get(format("/proxy/%s?url=%s", protocol, host)))
                    .andExpect(matcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
