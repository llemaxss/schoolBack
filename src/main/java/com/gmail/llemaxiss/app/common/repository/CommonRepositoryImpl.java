package com.gmail.llemaxiss.app.common.repository;

import com.gmail.llemaxiss.app.common.entity.CommonEntity;
import com.gmail.llemaxiss.app.common.security.util.SecurityUtil;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.time.Instant;
import java.util.UUID;

/**
 * Base repository interface implementation for all entities extending {@link CommonEntity}
 *
 * <p>
 * This implementation overrides standard delete methods to prevent accidental hard deletion.
 * Instead, it provides explicit {@code softDelete} methods to mark entities as deleted
 * by updating {@code deleteTs} and {@code deletedBy} fields.
 * </p>
 */
public class CommonRepositoryImpl<E extends CommonEntity>
  extends SimpleJpaRepository<E, UUID>
  implements CommonRepository<E> {
  
  public CommonRepositoryImpl(JpaEntityInformation<E, ?> entityInformation,
                              EntityManager entityManager) {
    super(entityInformation, entityManager);
  }
  
  /**
   * Replace standard hard-deletion to soft-deletion
   *
   * @see #softDelete(CommonEntity)
   */
  @Override
  public void delete(E entity) {
    softDelete(entity);
  }
  
  /**
   * @deprecated
   *
   * <p>
   * User {@link #delete(CommonEntity)} instead of,
   * or remake this method for working with soft-deleted conception
   * </p>
   */
  @Deprecated
  @Override
  public void deleteAllByIdInBatch(Iterable<UUID> uuids) {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }
  
  /**
   * @deprecated
   *
   * <p>
   * User {@link #delete(CommonEntity)} instead of,
   * or remake this method for working with soft-deleted conception
   * </p>
   */
  @Deprecated
  @Override
  public void deleteAllInBatch(Iterable<E> entities) {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }
  
  /**
   * @deprecated
   *
   * <p>
   * User {@link #delete(CommonEntity)} instead of,
   * or remake this method for working with soft-deleted conception
   * </p>
   */
  @Deprecated
  @Override
  public void deleteAllInBatch() {
    throw new UnsupportedOperationException("This method is not implemented yet");
  }
  
  private void softDelete(E entity) {
    entity.setDeleteTs(Instant.now());
    entity.setDeletedBy(SecurityUtil.getCurrentUsername());
    
    super.save(entity);
  }
}
