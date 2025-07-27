package com.mofumo.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "providers")
public class Provider {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Size(max = 100)
  @NotNull
  @Column(name = "business_name", nullable = false, length = 100)
  private String businessName;

  @NotNull
  @Lob
  @Column(name = "description", nullable = false)
  private String description;

  @NotNull
  @Column(name = "service_types", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> serviceTypes;

  @NotNull
  @Column(name = "languages_spoken", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> languagesSpoken;

  @NotNull
  @Column(name = "service_areas", nullable = false)
  @JdbcTypeCode(SqlTypes.JSON)
  private Map<String, Object> serviceAreas;

  @ColumnDefault("0")
  @Column(name = "mobile_service")
  private Boolean mobileService;

  @Column(name = "base_price")
  private Integer basePrice;

  @ColumnDefault("0")
  @Column(name = "verified")
  private Boolean verified;

  @ColumnDefault("1")
  @Column(name = "accepts_new_clients")
  private Boolean acceptsNewClients;

  @ColumnDefault("1")
  @Column(name = "active")
  private Boolean active;

  @NotNull
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "provider")
  private Set<Booking> bookings = new LinkedHashSet<>();

  @OneToMany(mappedBy = "provider")
  private Set<ProviderAvailability> providerAvailabilities = new LinkedHashSet<>();

  @OneToMany(mappedBy = "provider")
  private Set<Service> services = new LinkedHashSet<>();

}