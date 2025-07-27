package com.mofumo.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
  @Id
  @Column(name = "id")
  private String id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @Column(name = "amount")
  private Integer amount;

  @ColumnDefault("'credit_card'")
  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "external_payment_id")
  private String externalPaymentId;

  @ColumnDefault("'pending'")
  @Column(name = "status")
  private String status;

  @Column(name = "payment_date")
  private LocalDate paymentDate;

  @Column(name = "createdAt")
  private Instant createdAt;

}