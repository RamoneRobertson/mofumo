package com.mofumo.api.entities;

import com.mofumo.api.enums.UserType;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone")
  private String phone;

  @Column(name = "line_id")
  private String lineId;

  @Column(name = "ward")
  private String ward;

  @Column(name = "user_type")
  private UserType userType;

  @Column(name = "preferred_lang")
  private String preferredLang;

  @Column(name = "email_verified")
  private Boolean emailVerified;

  @Column(name = "active")
  private Boolean active;

  @Column(name = "createdAt")
  private Timestamp createdAt;

  @Column(name = "updatedAt")
  private Timestamp updatedAt;

  // Pets, Reviews, Providers, Bookings
  private Set<Pet> pets = new LinkedHashSet<>();
  private Set<Booking> bookings = new LinkedHashSet<>();
  private Set<Review> reviews = new LinkedHashSet<>();
  private Set<Provider> providers = new LinkedHashSet<>();
}
