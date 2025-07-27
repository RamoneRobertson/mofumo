package com.mofumo.api;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "provider_availability")
public class ProviderAvailability {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "provider_id", nullable = false)
  private Provider provider;

  @Lob
  @Column(name = "day_of_week")
  private String dayOfWeek;

  @Column(name = "available_date")
  private LocalDate availableDate;

  @NotNull
  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @NotNull
  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @NotNull
  @ColumnDefault("0")
  @Column(name = "spans_midnight", nullable = false)
  private Boolean spansMidnight = false;

  @NotNull
  @Lob
  @Column(name = "recurrence_type", nullable = false)
  private String recurrenceType;

  @NotNull
  @Column(name = "valid_from", nullable = false)
  private LocalDate validFrom;

  @Column(name = "valid_until")
  private LocalDate validUntil;

  @NotNull
  @ColumnDefault("0")
  @Column(name = "is_blocked", nullable = false)
  private Boolean isBlocked = false;

  @NotNull
  @ColumnDefault("0")
  @Column(name = "is_exception", nullable = false)
  private Boolean isException = false;

  @Size(max = 255)
  @Column(name = "notes")
  private String notes;

  @NotNull
  @ColumnDefault("1")
  @Column(name = "active", nullable = false)
  private Boolean active = false;

  @NotNull
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updated_at")
  private Instant updatedAt;

}