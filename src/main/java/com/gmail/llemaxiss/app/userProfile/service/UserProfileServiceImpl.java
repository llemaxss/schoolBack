package com.gmail.llemaxiss.app.userProfile.service;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app._common.service.CommonService;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.mapper.UserProfileMapper;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileCreateModel;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileUpdateModel;
import com.gmail.llemaxiss.app.userProfile.repository.UserProfileRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl extends CommonService implements UserProfileService {
  
  private final UserProfileRepository userProfileRepository;
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public UserProfile getUserProfileById(@NotNull UUID id) {
    Optional<UserProfile> userProfileOptional = userProfileRepository.findById(id);
    
    if (userProfileOptional.isEmpty()) {
      String message = String.format("UserProfile by id %s not found", id);
      throw new CommonException(ErrorCode.USER_PROFILE_NOT_FOUND, message);
    }
    
    return userProfileOptional.get();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public UserProfile createUserProfile(@NotNull UserProfileCreateModel model) {
    validateExistsByUser(model.getUser().getId());
    
    UserProfile newUserProfile = new UserProfile();
    
    UserProfileMapper.fillEntityByUserProfileCreateModel(newUserProfile, model, em);
    
    newUserProfile = userProfileRepository.save(newUserProfile);
    
    log.info("UserProfile created successfully with id: {}", newUserProfile.getId());
    
    return newUserProfile;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public UserProfile updateUserProfile(@NotNull UUID id, @NotNull UserProfileUpdateModel model) {
    log.info("Updating UserProfile with id: {}", id);
    
    UserProfile userProfile = getUserProfileById(id);
    
    UUID oldUserId = userProfile.getUser().getId();
    UUID newUserId = model.getUser().getId();
    
    boolean isUserChanged = !oldUserId.equals(newUserId);
    
    if (isUserChanged) {
      validateExistsByUser(newUserId);
    }
    
    UserProfileMapper.fillEntityByUserProfileUpdateModel(userProfile, model, em);
    
    UserProfile updatedUserProfile = userProfileRepository.save(userProfile);
    
    log.info("UserProfile updated successfully with id: {}", updatedUserProfile.getId());
    
    return updatedUserProfile;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void deleteUserProfile(@NotNull UUID id) {
    log.info("Deleting UserProfile with id: {}", id);
    
    UserProfile userProfile = getUserProfileById(id);
    
    userProfileRepository.delete(userProfile);
    
    log.info("UserProfile deleted successfully with id: {}", id);
  }
  
  private void validateExistsByUser(@NotNull UUID userId) {
    boolean isExistsByUser = userProfileRepository.existsByUserId(userId);
    
    if (isExistsByUser) {
      String message = String.format("UserProfile for User %s already exists", userId);
      throw new CommonException(ErrorCode.USER_PROFILE_ALREADY_EXISTS, message);
    }
  }
  
}
