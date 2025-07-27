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

@Getter
@Setter
@Entity
@Table(name = "services")
public class Service {
  @Id
  @Size(max = 16)
  @Column(name = "id", nullable = false, length = 16)
  private String id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "provider_id", nullable = false)
  private Provider provider;

  @Size(max = 50)
  @NotNull
  @Column(name = "service_name", nullable = false, length = 50)
  private String serviceName;

  @Lob
  @Column(name = "service_category")
  private String serviceCategory;

  @NotNull
  @Column(name = "duration_minutes", nullable = false)
  private Integer durationMinutes;

  @NotNull
  @Column(name = "price", nullable = false)
  private Integer price;

  @NotNull
  @ColumnDefault("1")
  @Column(name = "active", nullable = false)
  private Boolean active = false;

  @NotNull
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "createdAt", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "service")
  private Set<Booking> bookings = new LinkedHashSet<>();

}