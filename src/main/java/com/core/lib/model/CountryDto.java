package com.core.lib.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CountryDto {

    private Long countryId;

    private String countryCode;

    private String countryName;

    private String dialingCode;

    private String currencyCode;
}
