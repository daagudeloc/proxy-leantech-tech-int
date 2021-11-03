package com.leantech.proxy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leantech.proxy.error.Error;
import com.leantech.proxy.util.ProxyUtils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.resolve;

@RestControllerAdvice
public class CustomErrorController implements ErrorController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleError(HttpServletRequest req, Exception ex) throws JsonProcessingException {
        System.out.println("Request: " + req.getRequestURL() + " raised " + ex);

        final String errMessage = ex.getMessage();
        final String errJson = errMessage.substring(errMessage.indexOf('"') + 1, errMessage.length() - 1);
        final Error reqErr = ProxyUtils.getMapper().readValue(errJson, Error.class);

        return new ResponseEntity(reqErr, resolve(reqErr.getCode()));
    }

    @ResponseBody
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Error> handleError(HttpServletRequest req, ResourceAccessException ex) {
        System.out.println("Request: " + req.getRequestURL() + " raised " + ex);

        return new ResponseEntity(new Error(INTERNAL_SERVER_ERROR.value(), ex.getMessage()), INTERNAL_SERVER_ERROR);
    }

}
