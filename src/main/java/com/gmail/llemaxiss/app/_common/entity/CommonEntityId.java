package com.gmail.llemaxiss.app._common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Common class for all entities with id
 */
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class CommonEntityId {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @NotNull
  @Column(name = "id", nullable = false)
  protected UUID id;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CommonEntityId that)) {
      return false;
    }

    return id != null
      && id.equals(that.getId());
  }

  @Override
  public int hashCode() {
    return id == null
      ? getClass().hashCode()
      : id.hashCode();
  }
  
}
