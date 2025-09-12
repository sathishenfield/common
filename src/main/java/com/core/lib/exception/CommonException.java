package com.core.lib.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

/**
 * CommonException
 * Base class for all custom exceptions in the application.
 * Extends RuntimeException so it can be thrown unchecked.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4087930070289447747L;

    private final String errorCode;     // Business or internal error code
    private final String errorMessage;  // User-friendly message (from message bundle)
    private final Integer code;         // HTTP status or app-specific numeric code
    private final String reason;        // Technical explanation (developer-friendly)

    // ---------------- Constructors ----------------

    /**
     * Default constructor.
     */
    public CommonException() {
        this(null, null, null, null, null);
    }

    /**
     * Constructor with only numeric code (e.g., HTTP status).
     */
    public CommonException(int code) {
        this(null, null, code, null, null);
    }

    /**
     * Constructor with only error code.
     */
    public CommonException(String errorCode) {
        this(errorCode, null, null, null, null);
    }

    /**
     * Constructor with error code and message.
     */
    public CommonException(String errorCode, String message) {
        this(errorCode, message, null, message, null);
    }

    /**
     * Constructor with error code and cause.
     */
    public CommonException(String errorCode, Throwable cause) {
        this(errorCode,
                cause != null ? cause.getMessage() : null,
                null,
                cause != null ? cause.getMessage() : null,
                cause);
    }

    /**
     * Constructor with error code, message, and reason.
     */
    public CommonException(String errorCode, String message, String reason) {
        this(errorCode, message, null, reason, null);
    }

    /**
     * Constructor with error code, message, and cause.
     */
    public CommonException(String errorCode, String message, Throwable cause) {
        this(errorCode,
                message,
                null,
                cause != null ? cause.getMessage() : message,
                cause);
    }

    /**
     * Master constructor (all fields).
     * Marked as protected so subclasses can reuse it.
     */
    protected CommonException(String errorCode, String errorMessage, Integer code,
                              String reason, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.code = code;
        this.reason = reason;
    }
}
