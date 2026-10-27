package com.gmail.llemaxiss.app.userContact.service;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app._common.service.CommonService;
import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import com.gmail.llemaxiss.app.userContact.mapper.UserContactMapper;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactCreateModel;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactUpdateModel;
import com.gmail.llemaxiss.app.userContact.repository.UserContactRepository;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.service.UserProfileService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserContactServiceImpl extends CommonService implements UserContactService {
  
  private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
  
  private static final Pattern PHONE_SEPARATORS_PATTERN = Pattern.compile("[\\s\\-()]");
  
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
  
  private static final Sort CONTACTS_SORT = Sort.by(
    Sort.Order.asc("type"),
    Sort.Order.desc("isPrimary"),
    Sort.Order.asc("createTs")
  );
  
  private final UserContactRepository userContactRepository;
  
  private final UserProfileService userProfileService;
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public UserContact getUserContactById(@NotNull UUID id) {
    Optional<UserContact> userContactOptional = userContactRepository.findById(id);
    
    if (userContactOptional.isEmpty()) {
      String message = String.format("UserContact by id %s not found", id);
      throw new CommonException(ErrorCode.USER_CONTACT_NOT_FOUND, message);
    }
    
    return userContactOptional.get();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional(readOnly = true)
  public List<UserContact> getUserContactsByUserProfileId(@NotNull UUID userProfileId) {
    userProfileService.getUserProfileById(userProfileId);
    
    return userContactRepository.findByUserProfileId(userProfileId, CONTACTS_SORT);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public UserContact createUserContact(@NotNull UserContactCreateModel model) {
    model.setValue(
      normalizeValue(model.getType(), model.getValue())
    );
    
    validateValue(model.getType(), model.getValue());
    
    UserProfile userProfile = userProfileService.getUserProfileById(
      model.getUserProfile().getId()
    );
    
    validateExistsByUserProfileAndTypeAndValue(
      userProfile.getId(), model.getType(), model.getValue()
    );
    
    if (BooleanUtils.isTrue(model.getIsPrimary())) {
      demotePrimary(
        userProfile.getId(), model.getType(), null
      );
    }
    
    UserContact newUserContact = new UserContact();
    
    newUserContact.setUserProfile(userProfile);
    
    UserContactMapper.fillEntityByUserContactCreateModel(newUserContact, model);
    
    newUserContact = userContactRepository.save(newUserContact);
    
    log.info("UserContact created successfully with id: {}", newUserContact.getId());
    
    return newUserContact;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Transactional
  public UserContact updateUserContact(@NotNull UUID id, @NotNull UserContactUpdateModel model) {
    log.info("Updating UserContact with id: {}", id);
    
    UserContact userContact = getUserContactById(id);
    
    model.setValue(
      normalizeValue(model.getType(), model.getValue())
    );
    
    validateValue(model.getType(), model.getValue());
    
    UUID userProfileId = userContact.getUserProfile()
      .getId();
    
    boolean isTypeChanged = userContact.getType() != model.getType();
    boolean isValueChanged = !userContact.getValue().equals(model.getValue());
    
    if (isTypeChanged || isValueChanged) {
      validateExistsByUserProfileAndTypeAndValue(
        userProfileId, model.getType(), model.getValue()
      );
    }
    
    if (BooleanUtils.isTrue(model.getIsPrimary())) {
      demotePrimary(
        userProfileId, model.getType(), userContact.getId()
      );
    }
    
    UserContactMapper.fillEntityByUserContactUpdateModel(userContact, model);
    
    UserContact updatedUserContact = userContactRepository.save(userContact);
    
    log.info("UserContact updated successfully with id: {}", updatedUserContact.getId());
    
    return updatedUserContact;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void deleteUserContact(@NotNull UUID id) {
    log.info("Deleting UserContact with id: {}", id);
    
    UserContact userContact = getUserContactById(id);
    
    userContactRepository.delete(userContact);
    
    log.info("UserContact deleted successfully with id: {}", id);
  }
  
  /**
   * Trims the value and brings it to a canonical form
   *
   * <p>
   * Phone: removes spaces, dashes and brackets. E-mail: lower case.
   * </p>
   */
  @NotNull
  private String normalizeValue(@NotNull ContactType type, @NotNull String value) {
    String trimmedValue = value.trim();
    
    return switch (type) {
      case PHONE ->
        PHONE_SEPARATORS_PATTERN.matcher(trimmedValue)
          .replaceAll("");
      case EMAIL ->
        trimmedValue.toLowerCase(Locale.ROOT);
    };
  }
  
  private void validateValue(@NotNull ContactType type, @NotNull String value) {
    Pattern pattern = switch (type) {
      case PHONE -> PHONE_PATTERN;
      case EMAIL -> EMAIL_PATTERN;
    };
    
    boolean isValid = pattern.matcher(value)
      .matches();
    
    if (!isValid) {
      String message = String.format("Value is not valid for contact type %s", type);
      throw new CommonException(ErrorCode.USER_CONTACT_VALUE_INVALID, message);
    }
  }
  
  private void validateExistsByUserProfileAndTypeAndValue(@NotNull UUID userProfileId,
                                                          @NotNull ContactType type,
                                                          @NotNull String value) {
    boolean isExists = userContactRepository.existsByUserProfileIdAndTypeAndValue(userProfileId, type, value);
    
    if (isExists) {
      String message = String.format(
        "UserContact of type %s with the same value already exists for UserProfile %s",
        type,
        userProfileId
      );
      throw new CommonException(ErrorCode.USER_CONTACT_ALREADY_EXISTS, message);
    }
  }
  
  /**
   * Demotes the current primary contact of the type (if it is not the {@code exceptId} contact)
   *
   * <p>
   * The flush is required: the partial unique index allows only one primary contact per type,
   * so the old one must be written to the database before the new one.
   * It also has to be called before changing the contact being updated,
   * otherwise Hibernate may write the updates in a different order.
   * </p>
   */
  private void demotePrimary(@NotNull UUID userProfileId, @NotNull ContactType type, UUID exceptId) {
    Optional<UserContact> primaryOptional = userContactRepository.findByUserProfileIdAndTypeAndIsPrimaryTrue(
      userProfileId,
      type
    );
    
    if (primaryOptional.isEmpty()) {
      return;
    }
    
    UserContact primary = primaryOptional.get();
    
    if (primary.getId().equals(exceptId)) {
      return;
    }
    
    primary.setIsPrimary(false);
    
    userContactRepository.saveAndFlush(primary);
  }
  
}