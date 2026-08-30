package com.gmail.llemaxiss.app.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Common class for all entities
 */
@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @NotNull
  @Column(name = "id", nullable = false)
  private UUID id;

  @Version
  @NotNull
  @Column(name = "version", nullable = false)
  private Long version;

  @CreationTimestamp
  @NotNull
  @Column(name = "create_ts", nullable = false)
  private Instant createTs;

  @NotNull
  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @UpdateTimestamp
  @Column(name = "update_ts")
  private Instant updateTs;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "delete_ts")
  private Instant deleteTs;

  @Column(name = "deleted_by")
  private String deletedBy;

  protected AbstractEntity() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof AbstractEntity that)) {
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
