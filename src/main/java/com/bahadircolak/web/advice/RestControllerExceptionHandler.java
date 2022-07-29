package com.getmidas.order.api.infrastructure.interceptor;

import com.getmidas.order.api.application.exception.MidasValidationException;
import com.getmidas.order.api.application.model.response.ErrorResponse;
import com.getmidas.order.api.domain.exception.MidasApiClientException;
import com.getmidas.order.api.domain.exception.MidasBusinessException;
import com.getmidas.order.api.domain.exception.MidasInternalException;
import com.getmidas.order.api.domain.model.dto.LocalizedMessageDto;
import com.getmidas.order.api.infrastructure.utils.MessageSourceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class RestControllerExceptionHandler {

    private final MessageSourceUtils messageSourceUtils;

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException exception, Locale locale) {
        log.warn("An exception occurred during processing of request: {}", getCurrentRequestMethodAndURL(), exception);
        LocalizedMessageDto localizedMessage = messageSourceUtils.getLocalizedMessage("request.validation.member.access.request.denied", locale);
        return generateErrorResponse(localizedMessage, "BusinessException");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException exception, Locale locale) {
        log.warn("An exception occurred during processing of request: {}", getCurrentRequestMethodAndURL(), exception);
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        LocalizedMessageDto localizedMessageDto = messageSourceUtils.getLocalizedMessage(allErrors.get(0).getDefaultMessage(), locale);
        Map<String, String> hashMap = new HashMap<>();
        for (ObjectError error : allErrors) {
            String field = ((FieldError) error).getField();
            String fieldErrorMessage = messageSourceUtils.getLocalizedMessage(error.getDefaultMessage(), locale).getMessage();
            hashMap.put(field, fieldErrorMessage);
        }
        ErrorResponse errorResponse = generateErrorResponse(localizedMessageDto, "ValidationError");
        errorResponse.setErrors(hashMap);
        return errorResponse;
    }

    @ExceptionHandler(value = {MidasValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MidasValidationException exception, Locale locale) {
        log.warn("An exception occurred during processing of request: {}", getCurrentRequestMethodAndURL(), exception);
        LocalizedMessageDto localizedMessage = messageSourceUtils.getLocalizedMessage(exception.getMessage(), exception.getParameters(), locale);
        return generateErrorResponse(localizedMessage, "ValidationException");
    }

    @ExceptionHandler(value = {MidasBusinessException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBusinessException(MidasBusinessException exception, Locale locale) {
        log.warn("An exception occurred during processing of request: {}", getCurrentRequestMethodAndURL(), exception);
        LocalizedMessageDto localizedMessage = messageSourceUtils.getLocalizedMessage(exception.getMessage(), exception.getParameters(), locale);
        return generateErrorResponse(localizedMessage, "BusinessException");
    }

    @ExceptionHandler(value = {MidasApiClientException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleApiClientException(MidasApiClientException exception, Locale locale) {
        log.error("An exception occurred during processing of request: {}", getCurrentRequestMethodAndURL(), exception);
        LocalizedMessageDto localizedMessage = messageSourceUtils.getLocalizedMessage(exception.getMessage(), exception.getParameters(), locale);
        return generateErrorResponseForInternal(localizedMessage, "SystemException", exception.getInternalMessage());
    }

    @ExceptionHandler(value = {MidasInternalException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalException(MidasInternalException exception, Locale locale) {
        log.error("An exception occurred during processing of request: {}", getCurrentRequestMethodAndURL(), exception);
        LocalizedMessageDto localizedMessage = messageSourceUtils.getLocalizedMessage(exception.getMessage(), exception.getParameters(), locale);
        return generateErrorResponseForInternal(localizedMessage, "SystemException", exception.getInternalMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception, Locale locale) {
        log.error("An exception occurred during processing of request: {}", getCurrentRequestMethodAndURL(), exception);
        LocalizedMessageDto localizedMessage = messageSourceUtils.getLocalizedMessage("system.exception", locale);
        return generateErrorResponse(localizedMessage, "SystemException");
    }

    private ErrorResponse generateErrorResponseForInternal(LocalizedMessageDto localizedMessage, String exceptionType, String internalMessage) {
        ErrorResponse errorResponse = generateErrorResponse(localizedMessage, exceptionType);
        errorResponse.setInternalMessage(internalMessage);
        return errorResponse;
    }

    private ErrorResponse generateErrorResponse(LocalizedMessageDto localizedMessageDto, String exceptionType) {
        return ErrorResponse.builder()
                .traceId(localizedMessageDto.getTraceId())
                .code(localizedMessageDto.getCode())
                .message(localizedMessageDto.getMessage())
                .type(exceptionType)
                .build();
    }

    private String getCurrentRequestMethodAndURL() {
        return getCurrentHttpServletRequest()
                .map(httpServletRequest -> httpServletRequest.getMethod() + ":" + httpServletRequest.getRequestURL())
                .orElse(Strings.EMPTY);
    }

    private Optional<HttpServletRequest> getCurrentHttpServletRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

}