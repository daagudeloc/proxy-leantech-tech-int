package com.leantech.proxy.controller;

import com.leantech.proxy.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ProxyController {

    public static final String PROXY_PATH = "/proxy/{protocol}";
    private final ProxyService proxyService;

    @Autowired
    public ProxyController(final ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping(value = PROXY_PATH)
    public ModelAndView getProxiedUrl(@PathVariable final String protocol, @RequestParam final String url) {
        return proxyService.getProxiedUrl(protocol, url);
    }

}
