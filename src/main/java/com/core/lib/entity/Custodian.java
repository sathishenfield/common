package com.core.lib.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "custodian")
@Getter
@Setter
public class Custodian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custodian_id")
    private Long custodianId;

    private String name;

    @Column(name = "registration_no")
    private String registrationNo;

    private String country;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false,nullable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "custodian", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Client> clients;
}
