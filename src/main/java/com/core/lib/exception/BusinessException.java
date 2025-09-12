package com.core.lib.exception;

import com.core.lib.constant.ErrorConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

/**
 * BusinessException
 * Represents exceptions caused by business rule violations.
 * Inherits all base exception details from {@link CommonException}.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends CommonException {

    @Serial
    private static final long serialVersionUID = 4087930070289795747L;

    private static final int FIXED_CODE = ErrorConstant.BUSINESS_ERROR_CODE;

    // -------------------- Constructors --------------------

    /**
     * Default constructor.
     */
    public BusinessException() {
        super(null, null, FIXED_CODE, null, null);
    }

    /**
     * Business exception with error code.
     */
    public BusinessException(String errorCode) {
        super(errorCode, null, FIXED_CODE, null, null);
    }

    /**
     * Business exception with error code and message.
     */
    public BusinessException(String errorCode, String message) {
        super(errorCode, message, FIXED_CODE, message, null);
    }

    /**
     * Business exception with error code and underlying cause.
     */
    public BusinessException(String errorCode, Throwable cause) {
        super(errorCode,
                cause != null ? cause.getMessage() : null,
                FIXED_CODE,
                cause != null ? cause.getMessage() : null,
                cause);
    }

    /**
     * Business exception with error code, message, and cause.
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(errorCode,
                message,
                FIXED_CODE,
                cause != null ? cause.getMessage() : message,
                cause);
    }
}
