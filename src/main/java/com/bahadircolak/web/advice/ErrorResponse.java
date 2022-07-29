package com.bahadircolak.web.advice;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;
}