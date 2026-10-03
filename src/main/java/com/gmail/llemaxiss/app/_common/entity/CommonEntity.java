package com.gmail.llemaxiss.app._common.entity;

import com.gmail.llemaxiss.app._common.hibernateFilter.util.HibernateFilterConstants;
import jakarta.persistence.Column;
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
public abstract class CommonEntity extends CommonEntityId {

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
  
}
