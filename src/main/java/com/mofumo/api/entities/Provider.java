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
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "business_name")
  private String businessName;

  @Column(name = "address")
  private String address;

  @Column(name = "description")
  private String description;

  @Column(name = "mobile_service")
  private Boolean mobileService;

  @Column(name = "base_price")
  private Integer basePrice;

  @Column(name = "verified")
  private Boolean verified;

  @Column(name = "accepts_new_clients")
  private Boolean acceptsNewClients;

  @Column(name = "active")
  private Boolean active;

  @Column(name = "created_at")
  private Instant createdAt;

  @OneToMany(mappedBy = "provider")
  private Set<Booking> bookings = new LinkedHashSet<>();

  @OneToMany(mappedBy = "provider")
  private Set<ProviderAvailability> timeSlots = new LinkedHashSet<>();

  @OneToMany(mappedBy = "provider")
  private Set<Service> services = new LinkedHashSet<>();

}