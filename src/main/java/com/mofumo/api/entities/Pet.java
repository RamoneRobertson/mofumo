package com.mofumo.api.entities;

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
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @Size(max = 25)
  @NotNull
  @Column(name = "name", nullable = false, length = 25)
  private String name;

  @NotNull
  @ColumnDefault("'OTHER'")
  @Lob
  @Column(name = "species", nullable = false)
  private String species;

  @Size(max = 100)
  @Column(name = "breed", length = 100)
  private String breed;

  @Column(name = "age_yr")
  private Integer ageYr;

  @Column(name = "weight_kg", precision = 5, scale = 2)
  private BigDecimal weightKg;

  @Lob
  @Column(name = "medical_conditions")
  private String medicalConditions;

  @Lob
  @Column(name = "special_instruction")
  private String specialInstruction;

  @Size(max = 100)
  @NotNull
  @Column(name = "emergency_contact_name", nullable = false, length = 100)
  private String emergencyContactName;

  @Size(max = 20)
  @NotNull
  @Column(name = "emergency_contact_phone", nullable = false, length = 20)
  private String emergencyContactPhone;

  @NotNull
  @ColumnDefault("1")
  @Column(name = "active", nullable = false)
  private Boolean active = false;

  @NotNull
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "createdAt", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "pet")
  private Set<Booking> bookings = new LinkedHashSet<>();

}