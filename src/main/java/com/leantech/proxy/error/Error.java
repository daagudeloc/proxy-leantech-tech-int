package com.leantech.proxy.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class Error {
    @JsonProperty("code")
    int code;
    @JsonProperty("description")
    String description;
}
