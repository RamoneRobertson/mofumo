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
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "provider_availability")
public class ProviderAvailability {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provider_id")
  private Provider provider;

  @Column(name = "day_of_week")
  private String dayOfWeek;

  @Column(name = "available_date")
  private LocalDate availableDate;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;

  @Column(name = "spans_midnight")
  private Boolean spansMidnight = false;

  @Column(name = "recurrence_type")
  private String recurrenceType;

  @Column(name = "valid_from")
  private LocalDate validFrom;

  @Column(name = "valid_until")
  private LocalDate validUntil;

  @Column(name = "is_blocked")
  private Boolean isBlocked = false;

  @Column(name = "is_exception")
  private Boolean isException = false;

  @Column(name = "notes")
  private String notes;

  @Column(name = "active")
  private Boolean active = false;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

}