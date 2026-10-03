package com.gmail.llemaxiss.app._common.repository;

import com.gmail.llemaxiss.app._common.entity.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Base repository interface for all entities extending {@link CommonEntity}
 */
@NoRepositoryBean
public interface CommonRepository<E extends CommonEntity> extends JpaRepository<E, UUID> {
}
