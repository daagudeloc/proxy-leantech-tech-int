package com.leantech.proxy.service;

import com.leantech.proxy.util.ProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import static java.lang.String.format;

@Service
public class ProxyService {

    private final RestTemplate restTemplate;

    @Autowired
    public ProxyService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ModelAndView getProxiedUrl(final String protocol, final String url) {

        final String fullURL = format("%s://%s", protocol, url);
        final HttpHeaders headersToLog = restTemplate
                .exchange(fullURL, HttpMethod.GET, new HttpEntity<>(""), String.class)
                .getHeaders();

        ProxyUtils.getLogger().warn(format("Proxied URL: %s", fullURL));
        ProxyUtils.logHeaders(headersToLog);

        return new ModelAndView(format("redirect:%s", fullURL));
    }

}
