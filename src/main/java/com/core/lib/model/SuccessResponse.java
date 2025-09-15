package com.core.lib.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SuccessResponse {
    private LocalDateTime timestamp;
    private String status;
    private String code;
    private String message;
    private Object data;
}
