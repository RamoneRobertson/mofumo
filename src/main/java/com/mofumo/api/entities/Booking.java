package com.mofumo.api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
  @Column(name = "id")
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pet_id")
  private Pet pet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provider_id")
  private Provider provider;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "booking_date")
  private LocalDate bookingDate;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;

  @Column(name = "location_type")
  private String locationType;

  @Column(name = "special_requests")
  private String specialRequests;

  @Column(name = "total_price")
  private Integer totalPrice;

  @Column(name = "status")
  private String status;

  @Column(name = "updatedAt")
  private Instant updatedAt;

  @Column(name = "createdAt")
  private Instant createdAt;

  @OneToMany(mappedBy = "booking")
  private Set<Payment> payments = new LinkedHashSet<>();

  @OneToMany(mappedBy = "booking")
  private Set<Review> reviews = new LinkedHashSet<>();

}