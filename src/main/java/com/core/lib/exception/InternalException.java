package com.core.lib.exception;

import com.core.lib.constant.ErrorConstant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InternalException extends CommonException {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int FIXED_CODE = ErrorConstant.INTERNAL_ERROR_CODE;

    public InternalException() {
        super(null, null, FIXED_CODE, null, null);
    }

    public InternalException(String errorCode) {
        super(errorCode, null, FIXED_CODE, null, null);
    }

    public InternalException(String errorCode, String message) {
        super(errorCode, message, FIXED_CODE, message, null);
    }

    public InternalException(String errorCode, Throwable cause) {
        super(errorCode,
                cause != null ? cause.getMessage() : null,
                FIXED_CODE,
                cause != null ? cause.getMessage() : null,
                cause);
    }

    public InternalException(String errorCode, String message, Throwable cause) {
        super(errorCode,
                message,
                FIXED_CODE,
                cause != null ? cause.getMessage() : message,
                cause);
    }
}
