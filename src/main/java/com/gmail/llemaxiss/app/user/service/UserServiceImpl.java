package com.gmail.llemaxiss.app.user.service;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import com.gmail.llemaxiss.app.common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app.common.security.model.response.AppUserDetails;
import com.gmail.llemaxiss.app.common.security.util.SecurityUtil;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.repository.UserRepository;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  /**
   * {@inheritDoc}
   */
  @Override
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
  public User getUserByUsername(@NotNull String username) {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      String message = String.format("User by username %s not found", username);
      throw new CommonException(ErrorCode.USER_NOT_FOUND, message);
    }

    return user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public User getCurrentUser() {
    String userName = SecurityUtil.getCurrentUsername();
    
    return getUserByUsername(userName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public AppUserDetails getCurrentUserDetails() {
    User user = getCurrentUser();

    return new AppUserDetails(user);
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
  
}
