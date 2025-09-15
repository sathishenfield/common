package com.core.lib.util;

import com.core.lib.model.SuccessResponse;

import java.time.LocalDateTime;

public class CommonUtil {

    public static SuccessResponse getSuccessResponse(String code, String message, Object data) {
        return SuccessResponse.builder()
                .timestamp(LocalDateTime.now())
                .status("SUCCESS")
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static SuccessResponse getErrorResponse(String errorCode, String message, Object data) {
        return SuccessResponse.builder()
                .code(errorCode)
                .status("ERROR")
                .message(message)
                .data(data)
                .build();
    }
}
