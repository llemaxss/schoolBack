package com.gmail.llemaxiss.app._common.entity;

import com.gmail.llemaxiss.app._common.hibernateFilter.util.HibernateFilterConstants;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Common class for all entities
 */
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Filters({
  @Filter(name = HibernateFilterConstants.SOFT_DELETE_FILTER_NAME)
})
public abstract class CommonEntity extends CommonEntityId {

  @Version
  @NotNull
  @Column(name = "version", nullable = false)
  protected Long version;
  
  @CreatedDate
  @NotNull
  @Column(name = "create_ts", nullable = false)
  protected Instant createTs;
  
  @CreatedBy
  @NotNull
  @Column(name = "created_by", nullable = false)
  protected String createdBy;
  
  @LastModifiedDate
  @Column(name = "update_ts")
  protected Instant updateTs;
  
  @LastModifiedBy
  @Column(name = "updated_by")
  protected String updatedBy;

  @Column(name = "delete_ts")
  protected Instant deleteTs;

  @Column(name = "deleted_by")
  protected String deletedBy;
  
  /**
   * Prevents accidental hard deletion via {@code EntityManager#remove}
   *
   * <p>
   * Use {@code CommonRepository#delete} instead, which performs soft deletion.
   * </p>
   */
  @PreRemove
  protected void preventHardDelete() {
    throw new UnsupportedOperationException(
      "Hard deletion is forbidden, use repository delete() for soft deletion"
    );
  }
  
}
