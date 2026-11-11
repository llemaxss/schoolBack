package com.gmail.llemaxiss.app.user.service;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app._common.hibernateFilter.component.HibernateFilterManager;
import com.gmail.llemaxiss.app._common.security.model.response.AppUserDetails;
import com.gmail.llemaxiss.app._common.service.CommonService;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.mapper.UserMapper;
import com.gmail.llemaxiss.app.user.model.request.UserCreateModel;
import com.gmail.llemaxiss.app.user.model.request.UserUpdateModel;
import com.gmail.llemaxiss.app.user.repository.UserRepository;
import com.gmail.llemaxiss.app.user.util.UserConstants;
import com.gmail.llemaxiss.app.user.util.UserUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends CommonService implements UserService {

  private final UserRepository userRepository;
  
  private final PasswordEncoder passwordEncoder;
  
  private final HibernateFilterManager hibernateFilterManager;

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      User user = getUserByUsername(username);

      return new AppUserDetails(user);
    } catch (Exception e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public User getUserByUsername(@NotBlank String username) {
    String normalizedUsername = normalizeUsername(username);
    
    Optional<User> userOptional = userRepository.findByUsername(normalizedUsername);

    if (userOptional.isEmpty()) {
      String message = String.format("User by username %s not found", normalizedUsername);
      throw new CommonException(ErrorCode.USER_NOT_FOUND, message);
    }

    return userOptional.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public User getCurrentUser() {
    AppUserDetails appUserDetails = UserUtil.getAppUserDetails();
    
    return getUserById(appUserDetails.getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public User getUserById(@NotNull UUID id) {
    Optional<User> userOptional = userRepository.findById(id);

    if (userOptional.isEmpty()) {
      String message = String.format("User by id %s not found", id);
      throw new CommonException(ErrorCode.USER_NOT_FOUND, message);
    }

    return userOptional.get();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public User getUserByIdIncludingInactive(@NotNull UUID id) {
    return hibernateFilterManager.executeWithoutActiveUserFilter(() ->
      getUserById(id)
    );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public User createUser(@NotNull UserCreateModel model) {
    model.setUsername(
      normalizeUsername(
        model.getUsername()
      )
    );
    
    validateNotExistsByUsername(
      model.getUsername()
    );
    
    User newUser = new User();
    
    UserMapper.fillEntityByUserCreateModel(newUser, model);
    
    newUser.setPassword(
      passwordEncoder.encode(model.getPassword())
    );
    newUser.setIsActive(true);
    
    newUser = userRepository.save(newUser);
    
    log.info("User created successfully with id: {}", newUser.getId());
    
    return newUser;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public User updateUser(@NotNull UUID id, @NotNull UserUpdateModel model) {
    log.info("Updating User with id: {}", id);
    
    User user = getUserByIdIncludingInactive(id);
    
    validateIsNotProtected(user);
    
    model.setUsername(
      normalizeUsername(
        model.getUsername()
      )
    );
    
    if (!user.getUsername().equals(model.getUsername())) {
      validateNotExistsByUsername(
        model.getUsername()
      );
    }
    
    UserMapper.fillEntityByUserUpdateModel(user, model);
    
    User updatedUser = userRepository.save(user);
    
    log.info("User updated successfully with id: {}", updatedUser.getId());
    
    return updatedUser;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void deleteUser(@NotNull UUID id) {
    log.info("Deleting User with id: {}", id);
    
    User user = getUserByIdIncludingInactive(id);
    
    validateIsNotProtected(user);
    validateIsNotCurrentUser(user);
    
    userRepository.delete(user);
    
    log.info("User deleted successfully with id: {}", id);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public User activateUser(@NotNull UUID id) {
    User user = getUserByIdIncludingInactive(id);
    
    return changeUserActivity(user, true);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public User deactivateUser(@NotNull UUID id) {
    User user = getUserByIdIncludingInactive(id);
    
    return changeUserActivity(user, false);
  }
  
  /**
   * Changes the activity of the {@link User}
   *
   * <p>
   * Nothing is written if the user already has the requested state.
   * </p>
   */
  @NotNull
  private User changeUserActivity(@NotNull User user, boolean isActive) {
    if (!isActive) {
      validateIsNotProtected(user);
      validateIsNotCurrentUser(user);
    }
    
    if (user.getIsActive().equals(isActive)) {
      log.info("User with id {} already has activity: {}", user.getId(), isActive);
      
      return user;
    }
    
    user.setIsActive(isActive);
    
    User savedUser = userRepository.save(user);
    
    log.info("User activity changed successfully, id: {}, isActive: {}", user.getId(), isActive);
    
    return savedUser;
  }
  
  private void validateNotExistsByUsername(@NotBlank String username) {
    boolean isExists = hibernateFilterManager.executeWithoutActiveUserFilter(() ->
      userRepository.existsByUsername(username)
    );
    
    if (isExists) {
      String message = String.format("User with username %s already exists", username);
      throw new CommonException(ErrorCode.USER_USERNAME_ALREADY_EXISTS, message);
    }
  }
  
  private void validateIsNotProtected(@NotNull User user) {
    if (UserConstants.PROTECTED_USER_IDS.contains(user.getId())) {
      String message = String.format(
        "User %s is a default user and cannot be changed, deleted or deactivated",
        user.getId()
      );
      throw new CommonException(ErrorCode.USER_PROTECTED, message);
    }
  }
  
  private void validateIsNotCurrentUser(@NotNull User user) {
    UUID currentUserId = UserUtil.getAppUserDetails()
      .getId();
    
    if (user.getId().equals(currentUserId)) {
      String message = "User cannot delete or deactivate himself";
      throw new CommonException(ErrorCode.USER_SELF_ACTION_FORBIDDEN, message);
    }
  }
  
  @NotBlank
  private String normalizeUsername(@NotBlank String username) {
    String trimmedUsername = StringUtils.trim(username);
    
    return StringUtils.lowerCase(trimmedUsername, Locale.ROOT);
  }
  
}
