package com.core.lib.dto.onboarding;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientContactDto {
    private Long contactId;

    private String contactType;

    private String contactValue;

    private Boolean isPrimary;
}
