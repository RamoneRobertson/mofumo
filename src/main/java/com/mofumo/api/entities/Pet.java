package com.mofumo.api.entities;

import com.mofumo.api.enums.Species;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "pets")
public class Pet {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private User user;

  @Column(name = "name")
  private String name;

  @Column(name = "species")
  @Enumerated(EnumType.STRING)
  private Species species;

  @Column(name = "breed")
  private String breed;

  @Column(name = "age_yr")
  private Integer ageYr;

  @Column(name = "weight_kg")
  private BigDecimal weightKg;

  @Column(name = "medical_conditions")
  private String medicalConditions;

  @Column(name = "special_instruction")
  private String specialInstruction;

  @Column(name = "emergency_contact_name")
  private String emergencyContactName;

  @Column(name = "emergency_contact_phone")
  private String emergencyContactPhone;

  @Column(name = "active")
  private Boolean active = false;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "pet")
  private Set<Booking> bookings = new LinkedHashSet<>();

  @PrePersist
  public void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.active = true;
  }

  @PreUpdate
  public void onUpdate() {
    this.updatedAt = Instant.now();
  }

}