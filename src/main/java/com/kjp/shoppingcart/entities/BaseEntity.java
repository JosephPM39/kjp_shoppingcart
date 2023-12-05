package com.kjp.shoppingcart.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity {
  @Id private UUID id;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @PrePersist
  protected void onCreate() {
    if (id == null) {
      id = UUID.randomUUID();
    }
    createdAt = Timestamp.valueOf(LocalDateTime.now());
    updatedAt = createdAt;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Timestamp.valueOf(LocalDateTime.now());
  }
}
