package com.getmidas.order.api.application.model.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Integer code;
    private String type;
    private String message;
    private String traceId;
    private Map<String, String> errors;
    private String internalMessage;
}