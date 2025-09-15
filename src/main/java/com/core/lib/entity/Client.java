package com.core.lib.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "tax_residency_country")
    private String taxResidencyCountry;

    @Column(name = "kyc_status")
    private String kycStatus;

    @Column(name = "risk_profile")
    private String riskProfile;

    @Column(name = "preferred_currency")
    private String preferredCurrency;

    @Column(name = "created_at", updatable = false,nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false,nullable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "custodian_id", nullable = false)
    @JsonBackReference
    private Custodian custodian;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientContact> contacts;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;
}
