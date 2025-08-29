package com.mofumo.api.entities;

import com.mofumo.api.enums.Role;
import com.mofumo.api.enums.Ward;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone")
  private String phone;

  @Column(name = "address")
  private String address;

  @Column(name = "line_id")
  private String lineId;

  @Column(name = "ward")
  @Enumerated(EnumType.STRING)
  private Ward ward;

  @Column(name = "preferred_lang")
  private String preferredLang;

  @Column(name = "email_verified")
  private Boolean emailVerified;

  @Column(name = "active")
  private Boolean active;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  // Pets, Reviews, Providers, Bookings
  @OneToMany(mappedBy = "owner")
  private Set<Pet> pets = new LinkedHashSet<>();

  @OneToMany(mappedBy = "user")
  private Set<Booking> bookings = new LinkedHashSet<>();

  @OneToMany(mappedBy = "user")
  private Set<Review> reviews = new LinkedHashSet<>();

  @OneToMany(mappedBy = "user")
  private Set<Provider> providers = new LinkedHashSet<>();

  @PrePersist
  public void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.emailVerified = false;
    this.active = true;
  }

  @PreUpdate
  public void onUpdate() {
    this.updatedAt = Instant.now();
  }
}
