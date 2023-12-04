package com.kjp.shoppingcart.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = Timestamp.valueOf(LocalDateTime.now());
    updatedAt = createdAt;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Timestamp.valueOf(LocalDateTime.now());
  }
}
