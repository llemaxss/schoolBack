package com.gmail.llemaxiss.app.common.entity;

import com.gmail.llemaxiss.app.common.hibernateFilter.util.HibernateFilterConstants;
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
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
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
@Filters({
  @Filter(name = HibernateFilterConstants.SOFT_DELETE_FILTER_NAME)
})
public abstract class CommonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @NotNull
  @Column(name = "id", nullable = false)
  protected UUID id;

  @Version
  @NotNull
  @Column(name = "version", nullable = false)
  protected Long version;

  @CreationTimestamp
  @NotNull
  @Column(name = "create_ts", nullable = false)
  protected Instant createTs;

  @NotNull
  @Column(name = "created_by", nullable = false)
  protected String createdBy;

  @UpdateTimestamp
  @Column(name = "update_ts")
  protected Instant updateTs;

  @Column(name = "updated_by")
  protected String updatedBy;

  @Column(name = "delete_ts")
  protected Instant deleteTs;

  @Column(name = "deleted_by")
  protected String deletedBy;

  protected CommonEntity() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CommonEntity that)) {
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
