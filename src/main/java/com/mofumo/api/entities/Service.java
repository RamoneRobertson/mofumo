package com.mofumo.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "services")
public class Service {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provider_id")
  private Provider provider;

  @Column(name = "service_name")
  private String serviceName;

  @Column(name = "service_category")
  private String serviceCategory;

  @Column(name = "duration_minutes")
  private Integer durationMinutes;

  @Column(name = "price")
  private Integer price;

  @Column(name = "active")
  private Boolean active = false;

  @Column(name = "createdAt")
  private Instant createdAt;

  @OneToMany(mappedBy = "service")
  private Set<Booking> bookings = new LinkedHashSet<>();

}