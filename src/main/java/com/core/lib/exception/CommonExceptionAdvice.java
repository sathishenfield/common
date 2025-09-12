package com.core.lib.exception;

import com.core.lib.constant.ErrorConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Optional;

@RestControllerAdvice
@Log4j2
public class CommonExceptionAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    private CommonMessage message;

    @Value("${api.service.code}")
    private String serviceCode;

    @Value("${business.errorcode.prefix}")
    private String businessErrorCodePrefix;

    @Value("${internal.errorcode.prefix}")
    private String internalErrorCodePrefix;

    // ------------------------- Client Errors -------------------------

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public @ResponseBody ExceptionResponse handleUnauthorized(HttpClientErrorException.Unauthorized ex,
                                                              HttpServletRequest request) {
        return buildResponseForHttpError(ex, request);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public @ResponseBody ExceptionResponse handleForbidden(HttpClientErrorException.Forbidden ex,
                                                           HttpServletRequest request) {
        return buildResponseForHttpError(ex, request);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public @ResponseBody ExceptionResponse handleNotFound(HttpClientErrorException.NotFound ex,
                                                          HttpServletRequest request) {
        return buildResponseForHttpError(ex, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class)
    public @ResponseBody ExceptionResponse handleClientError(HttpClientErrorException ex,
                                                             HttpServletRequest request) {
        return buildResponseForHttpError(ex, request);
    }

    // ------------------------- Server Errors -------------------------

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(HttpServerErrorException.BadGateway.class)
    public @ResponseBody ExceptionResponse handleBadGateway(HttpServerErrorException.BadGateway ex,
                                                            HttpServletRequest request) {
        return buildResponseForHttpError(ex, request);
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    public @ResponseBody ExceptionResponse handleGatewayTimeout(HttpServerErrorException.GatewayTimeout ex,
                                                                HttpServletRequest request) {
        return buildResponseForHttpError(ex, request);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpServerErrorException.class)
    public @ResponseBody ExceptionResponse handleHttpServerError(HttpServerErrorException ex,
                                                                 HttpServletRequest request) {
        return buildResponseForHttpError(ex, request);
    }

    // ------------------------- Custom Exceptions -------------------------

    @ResponseStatus(HttpStatus.PARTIAL_CONTENT) // 206
    @ExceptionHandler(BusinessException.class)
    public @ResponseBody ExceptionResponse handleBusinessException(BusinessException ex,
                                                                   HttpServletRequest request) {
        return buildCustomError(ex.getCode(),
                businessErrorCodePrefix,
                ex.getErrorCode(),
                message.getMessage(ex.getErrorCode()),
                ex.getReason(),
                ErrorConstant.BUSINESS_ERROR,
                ex,
                request);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalException.class)
    public @ResponseBody ExceptionResponse handleInternalException(InternalException ex,
                                                                   HttpServletRequest request) {
        return buildCustomError(ex.getCode(),
                internalErrorCodePrefix,
                ex.getErrorCode(),
                message.getMessage(ex.getErrorCode()),
                ex.getReason(),
                ErrorConstant.TECHNICAL_ERROR,
                ex,
                request);
    }

    // ------------------------- Fallback -------------------------

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ExceptionResponse handleAllOtherExceptions(Exception ex,
                                                                    HttpServletRequest request) {
        return buildCustomError(ErrorConstant.INTERNAL_ERROR_CODE,
                internalErrorCodePrefix,
                "S500",
                message.getMessage("S500"),
                Optional.ofNullable(ex.getMessage()).orElse("Unexpected error"),
                ErrorConstant.TECHNICAL_ERROR,
                ex,
                request);
    }

    // ------------------------- Builders -------------------------

    private ExceptionResponse buildResponseForHttpError(HttpStatusCodeException ex, HttpServletRequest request) {
        int httpStatusCode = ex.getStatusCode().value();
        String errorCode = (httpStatusCode >= 500 ? ErrorConstant.SERVER_PREFIX : ErrorConstant.CLIENT_PREFIX)
                + httpStatusCode;

        return buildCustomError(httpStatusCode,
                internalErrorCodePrefix,
                errorCode,
                getErrorMessageFromException(ex),
                ex.getMessage(),
                ErrorConstant.TECHNICAL_ERROR,
                ex,
                request);
    }

    private ExceptionResponse buildCustomError(Integer httpCode,
                                               String prefix,
                                               String errorCode,
                                               String errorMessage,
                                               String reason,
                                               String errorType,
                                               Exception ex,
                                               HttpServletRequest request) {

        String fullErrorCode = serviceCode + "-" + prefix + "-" + errorCode;

        log.error("Exception handled | Code={} | ErrorCode={} | Reason={} | Path={} | Message={}",
                httpCode, fullErrorCode, reason, request.getRequestURI(), errorMessage, ex);

        return ExceptionResponse.builder()
                .code(httpCode)
                .errorCode(fullErrorCode)
                .errorMessage(errorMessage)
                .reason(reason)
                .errorType(errorType)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ------------------------- Utils -------------------------

    private String getErrorMessageFromException(HttpStatusCodeException exception) {
        try {
            JSONObject jsonObject = new JSONObject(exception.getResponseBodyAsString());
            if (jsonObject.has("message")) {
                return jsonObject.getString("message");
            } else if (jsonObject.has("reason")) {
                return jsonObject.getString("reason");
            }
        } catch (Exception e) {
            log.debug("Response not in JSON format for exception: {}", exception.getResponseBodyAsString());
        }
        return "Unexpected error occurred";
    }
}