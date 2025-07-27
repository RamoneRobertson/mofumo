package com.mofumo.api;

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
  @Size(max = 1)
  @Column(name = "id", nullable = false, length = 1)
  private String id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  @NotNull
  @Column(name = "amount", nullable = false)
  private Integer amount;

  @NotNull
  @ColumnDefault("'credit_card'")
  @Lob
  @Column(name = "payment_method", nullable = false)
  private String paymentMethod;

  @Size(max = 255)
  @NotNull
  @Column(name = "external_payment_id", nullable = false)
  private String externalPaymentId;

  @NotNull
  @ColumnDefault("'pending'")
  @Lob
  @Column(name = "status", nullable = false)
  private String status;

  @NotNull
  @ColumnDefault("(curdate())")
  @Column(name = "payment_date", nullable = false)
  private LocalDate paymentDate;

  @NotNull
  @ColumnDefault("CURRENT_TIMESTAMP")
  @Column(name = "createdAt", nullable = false)
  private Instant createdAt;

}