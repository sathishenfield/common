package com.core.lib.dto.onboarding;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Setter
@Getter
@ToString
public class ClientDto {

    private Long clientId;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private String panNumber;

    private String passportNumber;

    private String taxResidencyCountry;

    private String kycStatus;

    private String riskProfile;

    private String preferredCurrency;

    private List<ClientContactDto> contacts;

    private CountryDto country;

    private Long custodianId;
}
