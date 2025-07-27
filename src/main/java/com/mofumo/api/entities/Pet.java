package com.mofumo.api.entities;

import com.mofumo.api.enums.Species;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
  private User owner;

  @Column(name = "name")
  private String name;

  @Column(name = "species")
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

  @Column(name = "createdAt")
  private Instant createdAt;

  @OneToMany(mappedBy = "pet")
  private Set<Booking> bookings = new LinkedHashSet<>();

}