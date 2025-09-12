package com.core.lib.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Standard structure for API error responses.
 *
 * @param code            HTTP status or custom app code
 * @param errorCode       Business/internal error code (e.g., SVC-BIZ-1001)
 * @param errorMessage    User-friendly message (localized)
 * @param reason          Developer-friendly reason
 * @param detailedMessage Extra details (stacktrace snippet, root cause)
 * @param errorType       BUSINESS_ERROR, INTERNAL_ERROR, VALIDATION_ERROR, etc.
 * @param path            The request path that caused the error
 * @param timestamp       When error occurred
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // hide null fields in JSON
public record ExceptionResponse(Integer code, String errorCode, String errorMessage, String reason,
                                String detailedMessage, String errorType, String path, LocalDateTime timestamp) {

}
