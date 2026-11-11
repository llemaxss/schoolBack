package com.gmail.llemaxiss.app.integration._common;

import com.gmail.llemaxiss.app._common.hibernateFilter.component.HibernateFilterManager;
import com.gmail.llemaxiss.app._common.user.modelFactory.UserModelFactory;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Checks that the Hibernate filters (soft-delete, active user) work for all repository ways to load data
 * and that {@link HibernateFilterManager} changes them correctly
 *
 * <p>
 * Uses {@code User}: it is the only entity with both filters.
 * </p>
 */
public class CommonRepositoryIntegrationTest extends AbstractIntegrationTest {
  
  private static final String DELETED_USERNAME = "deleted.user";
  private static final String CURRENT_USERNAME = "current.user";
  
  private static final String TEST_AUTHOR = "test";
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private HibernateFilterManager hibernateFilterManager;
  
  @Autowired
  private TransactionTemplate transactionTemplate;
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  private UUID activeUserId;
  
  private UUID inactiveUserId;
  
  private UUID deletedUserId;
  
  @BeforeEach
  void setUpUsers() {
    activeUserId = insertUser(UserModelFactory.IVAN_USERNAME, true, false);
    inactiveUserId = insertUser(UserModelFactory.ALEXANDRA_USERNAME, false, false);
    deletedUserId = insertUser(DELETED_USERNAME, true, true);
  }
  
  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }
  
  //region FindById
  @Nested
  class FindById {
    
    @Test
    public void returnsActiveUser() {
      Optional<User> result = userRepository.findById(activeUserId);
      
      assertThat(result)
        .isPresent();
      
      assertThat(
        result.get()
          .getUsername()
      )
        .isEqualTo(
          UserModelFactory.IVAN_USERNAME
        );
    }
    
    @Test
    public void doesNotReturnInactiveUser() {
      Optional<User> result = userRepository.findById(inactiveUserId);
      
      assertThat(result)
        .isEmpty();
    }
    
    @Test
    public void doesNotReturnDeletedUser() {
      Optional<User> result = userRepository.findById(deletedUserId);
      
      assertThat(result)
        .isEmpty();
    }
    
    @Test
    public void returnsInactiveUserWithoutActiveUserFilter() {
      Optional<User> result = findByIdWithoutActiveUserFilter(inactiveUserId);
      
      assertThat(result)
        .isPresent();
      
      assertThat(
        result.get()
          .getUsername()
      )
        .isEqualTo(
          UserModelFactory.ALEXANDRA_USERNAME
        );
    }
    
    @Test
    public void doesNotReturnDeletedUserWithoutActiveUserFilter() {
      Optional<User> result = findByIdWithoutActiveUserFilter(deletedUserId);
      
      assertThat(result)
        .isEmpty();
    }
    
    @Test
    public void returnsDeletedUserWithoutSoftDeleteFilter() {
      Optional<User> result = findByIdWithoutSoftDeleteFilter(deletedUserId);
      
      assertThat(result)
        .isPresent();
      
      assertThat(
        result.get()
          .getUsername()
      )
        .isEqualTo(DELETED_USERNAME);
    }
    
  }
  //endregion FindById
  
  //region Queries
  @Nested
  class Queries {
    
    @Test
    public void findAllDoesNotReturnInactiveAndDeletedUsers() {
      List<UUID> ids = findAllIds();
      
      assertThat(ids)
        .contains(activeUserId)
        .doesNotContain(inactiveUserId, deletedUserId);
    }
    
    @Test
    public void findAllByIdReturnsOnlyActiveUser() {
      List<UUID> requestedIds = List.of(activeUserId, inactiveUserId, deletedUserId);
      
      List<User> result = userRepository.findAllById(requestedIds);
      
      List<UUID> ids = result.stream()
        .map(User::getId)
        .toList();
      
      assertThat(ids)
        .containsExactly(activeUserId);
    }
    
    @Test
    public void existsByIdIsTrueOnlyForActiveUser() {
      boolean isActiveExists = userRepository.existsById(activeUserId);
      boolean isInactiveExists = userRepository.existsById(inactiveUserId);
      boolean isDeletedExists = userRepository.existsById(deletedUserId);
      
      assertThat(isActiveExists)
        .isTrue();
      
      assertThat(isInactiveExists)
        .isFalse();
      
      assertThat(isDeletedExists)
        .isFalse();
    }
    
    @Test
    public void countDoesNotCountInactiveAndDeletedUsers() {
      Long expectedCount = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM school_user WHERE is_active = true AND delete_ts IS NULL",
        Long.class
      );
      
      long result = userRepository.count();
      
      assertThat(result)
        .isEqualTo(expectedCount);
    }
    
    @Test
    public void findByUsernameReturnsActiveUser() {
      Optional<User> result = userRepository.findByUsername(UserModelFactory.IVAN_USERNAME);
      
      assertThat(result)
        .isPresent();
      
      assertThat(
        result.get()
          .getId()
      )
        .isEqualTo(activeUserId);
    }
    
    @Test
    public void findByUsernameDoesNotReturnInactiveUser() {
      Optional<User> result = userRepository.findByUsername(UserModelFactory.ALEXANDRA_USERNAME);
      
      assertThat(result)
        .isEmpty();
    }
    
    @Test
    public void findByUsernameDoesNotReturnDeletedUser() {
      Optional<User> result = userRepository.findByUsername(DELETED_USERNAME);
      
      assertThat(result)
        .isEmpty();
    }
    
    @Test
    public void existsByUsernameIsFalseForDeletedUser() {
      boolean isActiveExists = userRepository.existsByUsername(UserModelFactory.IVAN_USERNAME);
      boolean isDeletedExists = userRepository.existsByUsername(DELETED_USERNAME);
      
      assertThat(isActiveExists)
        .isTrue();
      
      assertThat(isDeletedExists)
        .isFalse();
    }
    
  }
  //endregion Queries
  
  //region Delete
  @Nested
  class Delete {
    
    @Test
    public void deleteByIdSoftDeletesUser() {
      authenticateAsCurrentUser();
      
      userRepository.deleteById(activeUserId);
      
      Boolean isDeleted = jdbcTemplate.queryForObject(
        "SELECT delete_ts IS NOT NULL FROM school_user WHERE id = ?",
        Boolean.class,
        activeUserId
      );
      
      String deletedBy = jdbcTemplate.queryForObject(
        "SELECT deleted_by FROM school_user WHERE id = ?",
        String.class,
        activeUserId
      );
      
      Optional<User> found = userRepository.findById(activeUserId);
      
      assertThat(isDeleted)
        .isTrue();
      
      assertThat(deletedBy)
        .isEqualTo(CURRENT_USERNAME);
      
      assertThat(found)
        .isEmpty();
    }
    
    @Test
    public void deleteByIdOfDeletedUserDoesNothing() {
      authenticateAsCurrentUser();
      
      userRepository.deleteById(deletedUserId);
      
      String deletedBy = jdbcTemplate.queryForObject(
        "SELECT deleted_by FROM school_user WHERE id = ?",
        String.class,
        deletedUserId
      );
      
      assertThat(deletedBy)
        .isEqualTo(TEST_AUTHOR);
    }
    
  }
  //endregion Delete
  
  //region FilterModes
  @Nested
  class FilterModes {
    
    @Test
    public void deletedOnlyFilterShowsOnlyDeletedUsers() {
      List<UUID> ids = inTransaction(() ->
        hibernateFilterManager.executeWithSoftDeletedOnlyFilter(
          CommonRepositoryIntegrationTest.this::findAllIds
        )
      );
      
      assertThat(ids)
        .containsExactly(deletedUserId);
    }
    
    @Test
    public void withoutSoftDeleteFilterShowsDeletedUsers() {
      List<UUID> ids = inTransaction(() ->
        hibernateFilterManager.executeWithoutSoftDeleteFilter(
          CommonRepositoryIntegrationTest.this::findAllIds
        )
      );
      
      assertThat(ids)
        .contains(activeUserId, deletedUserId)
        .doesNotContain(inactiveUserId);
    }
    
    @Test
    public void notActiveUserOnlyFilterShowsOnlyInactiveUsers() {
      List<UUID> ids = inTransaction(() ->
        hibernateFilterManager.executeWithNotActiveUserOnlyFilter(
          CommonRepositoryIntegrationTest.this::findAllIds
        )
      );
      
      assertThat(ids)
        .containsExactly(inactiveUserId);
    }
    
    @Test
    public void withoutActiveUserFilterShowsInactiveUsers() {
      List<UUID> ids = inTransaction(() ->
        hibernateFilterManager.executeWithoutActiveUserFilter(
          CommonRepositoryIntegrationTest.this::findAllIds
        )
      );
      
      assertThat(ids)
        .contains(activeUserId, inactiveUserId)
        .doesNotContain(deletedUserId);
    }
    
    @Test
    public void filterIsRestoredAfterAction() {
      List<Boolean> visibility = inTransaction(
        CommonRepositoryIntegrationTest.this::inactiveUserVisibilityAroundBlock
      );
      
      // before the block, inside the block, after the block
      assertThat(visibility)
        .containsExactly(false, true, false);
    }
    
    @Test
    public void filterIsRestoredAfterException() {
      boolean isVisible = inTransaction(
        CommonRepositoryIntegrationTest.this::isInactiveUserVisibleAfterFailedAction
      );
      
      assertThat(isVisible)
        .isFalse();
    }
    
    @Test
    public void nestedActionKeepsStateOfOuterAction() {
      List<Boolean> visibility = inTransaction(() ->
        hibernateFilterManager.executeWithoutActiveUserFilter(
          CommonRepositoryIntegrationTest.this::inactiveUserVisibilityAroundBlock
        )
      );
      
      // the outer action has already disabled the filter, the inner one must not enable it back
      assertThat(visibility)
        .containsExactly(true, true, true);
    }
    
    @Test
    public void nestedActionRestoresNotDefaultMode() {
      List<Boolean> visibility = inTransaction(() ->
        hibernateFilterManager.executeWithNotActiveUserOnlyFilter(
          CommonRepositoryIntegrationTest.this::activeUserVisibilityAroundBlock
        )
      );
      
      // in the mode "only inactive" the active user is hidden, inside the block it is visible,
      // after the block the mode "only inactive" must be back (not the default one)
      assertThat(visibility)
        .containsExactly(false, true, false);
    }
    
  }
  //endregion FilterModes
  
  private UUID insertUser(@NotBlank String username, boolean isActive, boolean isDeleted) {
    UUID id = UUID.randomUUID();
    
    jdbcTemplate.update(
        "INSERT INTO school_user (id, version, create_ts, created_by, username, password, is_active) "
        + "VALUES (?, 1, now(), ?, ?, 'password', ?)",
      id,
      TEST_AUTHOR,
      username,
      isActive
    );
    
    if (isDeleted) {
      jdbcTemplate.update(
        "UPDATE school_user SET delete_ts = now(), deleted_by = ? WHERE id = ?",
        TEST_AUTHOR,
        id
      );
    }
    
    return id;
  }
  
  private void authenticateAsCurrentUser() {
    SecurityContextHolder.getContext()
      .setAuthentication(
        new UsernamePasswordAuthenticationToken(
          CURRENT_USERNAME,
          null,
          List.of()
        )
      );
  }
  
  /**
   * Filters can be changed only inside a transaction, so such calls are made through this method
   */
  private <T> T inTransaction(Supplier<T> action) {
    return transactionTemplate.execute(status ->
      action.get()
    );
  }
  
  private Optional<User> findByIdWithoutActiveUserFilter(UUID id) {
    return inTransaction(() ->
      hibernateFilterManager.executeWithoutActiveUserFilter(() ->
        userRepository.findById(id)
      )
    );
  }
  
  private Optional<User> findByIdWithoutSoftDeleteFilter(UUID id) {
    return inTransaction(() ->
      hibernateFilterManager.executeWithoutSoftDeleteFilter(() ->
        userRepository.findById(id)
      )
    );
  }
  
  private List<UUID> findAllIds() {
    return userRepository.findAll()
      .stream()
      .map(User::getId)
      .toList();
  }
  
  private boolean isInactiveUserVisible() {
    return userRepository.findById(inactiveUserId)
      .isPresent();
  }
  
  private boolean isActiveUserVisible() {
    return userRepository.findById(activeUserId)
      .isPresent();
  }
  
  /**
   * Visibility of the inactive user: before the block without the filter, inside it and after it
   */
  private List<Boolean> inactiveUserVisibilityAroundBlock() {
    boolean isVisibleBefore = isInactiveUserVisible();
    
    boolean isVisibleInside = hibernateFilterManager.executeWithoutActiveUserFilter(
      this::isInactiveUserVisible
    );
    
    boolean isVisibleAfter = isInactiveUserVisible();
    
    return List.of(isVisibleBefore, isVisibleInside, isVisibleAfter);
  }
  
  /**
   * Visibility of the active user: before the block without the filter, inside it and after it
   */
  private List<Boolean> activeUserVisibilityAroundBlock() {
    boolean isVisibleBefore = isActiveUserVisible();
    
    boolean isVisibleInside = hibernateFilterManager.executeWithoutActiveUserFilter(
      this::isActiveUserVisible
    );
    
    boolean isVisibleAfter = isActiveUserVisible();
    
    return List.of(isVisibleBefore, isVisibleInside, isVisibleAfter);
  }
  
  private boolean isInactiveUserVisibleAfterFailedAction() {
    assertThatThrownBy(() ->
      hibernateFilterManager.executeWithoutActiveUserFilter(this::failAction)
    )
      .isInstanceOf(IllegalStateException.class);
    
    return isInactiveUserVisible();
  }
  
  private Object failAction() {
    throw new IllegalStateException("Test exception");
  }
  
}