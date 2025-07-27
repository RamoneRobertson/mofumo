package com.mofumo.api;

import com.mofumo.api.entities.User;
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
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
  @Id
  @Size(max = 16)
  @Column(name = "id")
  private String id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "pet_id", nullable = false)
  private Pet pet;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "provider_id", nullable = false)
  private Provider provider;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "service_id", nullable = false)
  private Service service;

  @NotNull
  @ColumnDefault("(curdate())")
  @Column(name = "booking_date", nullable = false)
  private LocalDate bookingDate;

  @NotNull
  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @NotNull
  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @NotNull
  @ColumnDefault("'provider_location'")
  @Lob
  @Column(name = "location_type", nullable = false)
  private String locationType;

  @Lob
  @Column(name = "special_requests")
  private String specialRequests;

  @NotNull
  @Column(name = "total_price", nullable = false)
  private Integer totalPrice;

  @NotNull
  @ColumnDefault("'pending'")
  @Lob
  @Column(name = "status", nullable = false)
  private String status;

  @NotNull
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "updatedAt", nullable = false)
  private Instant updatedAt;

  @NotNull
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "createdAt", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "booking")
  private Set<Payment> payments = new LinkedHashSet<>();

  @OneToMany(mappedBy = "booking")
  private Set<Review> reviews = new LinkedHashSet<>();

}