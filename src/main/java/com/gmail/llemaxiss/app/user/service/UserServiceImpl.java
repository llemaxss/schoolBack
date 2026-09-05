package com.gmail.llemaxiss.app.user.service;
import com.gmail.llemaxiss.app.common.security.model.AppUserDetails;
import com.gmail.llemaxiss.app.common.security.util.SecurityUtil;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      User user = getUserByUsername(username);

      return new AppUserDetails(user);
    } catch (EntityNotFoundException e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public User getUserByUsername(@NotNull String username) throws EntityNotFoundException {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      String message = String.format("User by username %s not found", username);
      throw new EntityNotFoundException(message);
    }

    return user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public User getCurrentUser() throws EntityNotFoundException {
    try {
      String userName = SecurityUtil.getCurrentUsername();

      return getUserByUsername(userName);
    } catch (IllegalStateException e) {
      LOGGER.error(e.getMessage(), e);
      throw new EntityNotFoundException(e.getMessage(), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public AppUserDetails getCurrentUserDetails() throws EntityNotFoundException {
    User user = getCurrentUser();

    return new AppUserDetails(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public User getUserById(@NotNull UUID id) {
    Optional<User> userOptional = userRepository.findById(id);

    if (userOptional.isEmpty()) {
      String message = String.format("User by id %s not found", id);
      throw new EntityNotFoundException(message);
    }

    return userOptional.get();
  }
  
}
