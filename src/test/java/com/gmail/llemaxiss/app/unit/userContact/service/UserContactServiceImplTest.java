package com.gmail.llemaxiss.app.unit.userContact.service;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app.unit._common.CommonServiceImplTest;
import com.gmail.llemaxiss.app.unit.userContact.modelFactory.UserContactModelFactory;
import com.gmail.llemaxiss.app.unit.userProfile.modelFactory.UserProfileModelFactory;
import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactCreateModel;
import com.gmail.llemaxiss.app.userContact.model.request.UserContactUpdateModel;
import com.gmail.llemaxiss.app.userContact.repository.UserContactRepository;
import com.gmail.llemaxiss.app.userContact.service.UserContactServiceImpl;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserContactServiceImplTest extends CommonServiceImplTest {
  
  @Mock
  private UserContactRepository userContactRepository;
  
  @Mock
  private UserProfileService userProfileService;
  
  @Spy
  @InjectMocks
  private UserContactServiceImpl userContactService;
  
  @BeforeEach
  public void setUp() throws Exception {
    setUpEntityManager(userContactService);
  }
  
  //region Create
  @Nested
  class Create {
    
    @Test
    public void createPhoneSuccessfully() {
      UUID userProfileId = UUID.randomUUID();
      
      UserContactCreateModel model = UserContactModelFactory.ivanPhoneCreateModel();
      model.getUserProfile()
        .setId(userProfileId);
      
      UserContact savedUserContact = UserContactModelFactory.ivanPhoneEntity();
      savedUserContact.getUserProfile()
        .setId(userProfileId);
      
      when(
        userProfileService.getUserProfileById(
          userProfileId
        )
      )
        .thenReturn(
          savedUserContact.getUserProfile()
        );
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(savedUserContact);
      
      UserContact result = userContactService.createUserContact(model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(
          savedUserContact.getId()
        );
      
      assertThat(
        result.getUserProfile()
          .getId()
      )
        .isEqualTo(userProfileId);
      
      assertThat(
        result.getType()
      )
        .isEqualTo(
          model.getType()
        );
      
      assertThat(
        result.getValue()
      )
        .isEqualTo(
          model.getValue()
        );
      
      assertThat(
        result.getIsPrimary()
      )
        .isEqualTo(
          model.getIsPrimary()
        );
      
      assertThat(
        result.getComment()
      )
        .isEqualTo(
          model.getComment()
        );
      
      verify(userProfileService)
        .getUserProfileById(userProfileId);
      
      verify(userContactRepository)
        .existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          model.getType(),
          model.getValue()
        );
      
      verify(
        userContactRepository,
        never()
      )
        .findByUserProfileIdAndTypeAndIsPrimaryTrue(
          any(UUID.class),
          any(ContactType.class)
        );
      
      verify(userContactRepository)
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    public void createEmailSuccessfully() {
      UUID userProfileId = UUID.randomUUID();
      
      UserContactCreateModel model = UserContactModelFactory.ivanEmailCreateModel();
      model.getUserProfile()
        .setId(userProfileId);
      
      UserContact savedUserContact = UserContactModelFactory.ivanEmailEntity();
      savedUserContact.getUserProfile()
        .setId(userProfileId);
      
      when(
        userProfileService.getUserProfileById(
          userProfileId
        )
      )
        .thenReturn(
          savedUserContact.getUserProfile()
        );
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(savedUserContact);
      
      UserContact result = userContactService.createUserContact(model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(
          savedUserContact.getId()
        );
      
      assertThat(
        result.getUserProfile()
          .getId()
      )
        .isEqualTo(userProfileId);
      
      assertThat(
        result.getType()
      )
        .isEqualTo(
          model.getType()
        );
      
      assertThat(
        result.getValue()
      )
        .isEqualTo(
          model.getValue()
        );
      
      assertThat(
        result.getIsPrimary()
      )
        .isEqualTo(
          model.getIsPrimary()
        );
      
      assertThat(
        result.getComment()
      )
        .isEqualTo(
          model.getComment()
        );
      
      verify(userProfileService)
        .getUserProfileById(userProfileId);
      
      verify(userContactRepository)
        .existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          model.getType(),
          model.getValue()
        );
      
      verify(userContactRepository)
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    public void createPhoneNormalizesValue() {
      UUID userProfileId = UUID.randomUUID();
      
      UserContactCreateModel model = UserContactModelFactory.ivanPhoneCreateModel();
      model.getUserProfile()
        .setId(userProfileId);
      model.setValue(" +7 (900) 123-45-67 ");
      
      UserContact savedUserContact = UserContactModelFactory.ivanPhoneEntity();
      savedUserContact.getUserProfile()
        .setId(userProfileId);
      
      when(
        userProfileService.getUserProfileById(userProfileId)
      )
        .thenReturn(
          savedUserContact.getUserProfile()
        );
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          ContactType.PHONE,
          UserContactModelFactory.PHONE_INCREASE_VALUE
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(savedUserContact);
      
      userContactService.createUserContact(model);
      
      assertThat(
        model.getValue()
      )
        .isEqualTo(UserContactModelFactory.PHONE_INCREASE_VALUE);
      
      verify(userContactRepository)
        .existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          ContactType.PHONE,
          UserContactModelFactory.PHONE_INCREASE_VALUE
        );
    }
    
    @Test
    public void createEmailNormalizesValue() {
      UUID userProfileId = UUID.randomUUID();
      
      UserContactCreateModel model = UserContactModelFactory.ivanEmailCreateModel();
      model.getUserProfile()
        .setId(userProfileId);
      
      model.setValue(
        String.format(
          " %s ",
          UserContactModelFactory.IVAN_EMAIL_VALUE.toUpperCase()
        )
      );
      
      UserContact savedUserContact = UserContactModelFactory.ivanEmailEntity();
      savedUserContact.getUserProfile()
        .setId(userProfileId);
      
      when(
        userProfileService.getUserProfileById(userProfileId)
      )
        .thenReturn(
          savedUserContact.getUserProfile()
        );
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          ContactType.EMAIL,
          UserContactModelFactory.IVAN_EMAIL_VALUE
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(savedUserContact);
      
      userContactService.createUserContact(model);
      
      assertThat(
        model.getValue()
      )
        .isEqualTo(UserContactModelFactory.IVAN_EMAIL_VALUE);
      
      verify(userContactRepository)
        .existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          ContactType.EMAIL,
          UserContactModelFactory.IVAN_EMAIL_VALUE
        );
    }
    
    @Test
    public void createPrimaryDemotesOldPrimaryBeforeSave() {
      UUID userProfileId = UUID.randomUUID();
      
      UserContactCreateModel model = UserContactModelFactory.ivanPhoneCreateModel();
      model.getUserProfile()
        .setId(userProfileId);
      model.setIsPrimary(true);
      
      UserContact savedUserContact = UserContactModelFactory.ivanPhoneEntity();
      savedUserContact.getUserProfile()
        .setId(userProfileId);
      savedUserContact.setIsPrimary(true);
      
      UserContact oldPrimaryUserContact = UserContactModelFactory.alexandraPhoneEntity();
      oldPrimaryUserContact.setIsPrimary(true);
      
      when(
        userProfileService.getUserProfileById(userProfileId)
      )
        .thenReturn(
          savedUserContact.getUserProfile()
        );
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.findByUserProfileIdAndTypeAndIsPrimaryTrue(
          userProfileId,
          model.getType()
        )
      )
        .thenReturn(
          Optional.of(oldPrimaryUserContact)
        );
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(savedUserContact);
      
      UserContact result = userContactService.createUserContact(model);
      
      assertThat(
        result.getIsPrimary()
      )
        .isTrue();
      
      assertThat(
        oldPrimaryUserContact.getIsPrimary()
      )
        .isFalse();
      
      InOrder inOrder = inOrder(userContactRepository);
      
      inOrder.verify(userContactRepository)
        .saveAndFlush(oldPrimaryUserContact);
      
      inOrder.verify(userContactRepository)
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    void userContactAlreadyExistsCommonException() {
      UUID userProfileId = UUID.randomUUID();
      
      UserContactCreateModel model = UserContactModelFactory.ivanPhoneCreateModel();
      model.getUserProfile()
        .setId(userProfileId);
      
      UserProfile userProfile = UserProfileModelFactory.ivanEntity();
      userProfile.setId(userProfileId);
      
      when(
        userProfileService.getUserProfileById(userProfileId)
      )
        .thenReturn(userProfile);
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          userProfileId,
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(true);
      
      assertThatThrownBy(() -> {
        userContactService.createUserContact(model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_CONTACT_ALREADY_EXISTS
        );
      
      verify(
        userContactRepository,
        never()
      )
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    void userContactValueInvalidCommonException() {
      UserContactCreateModel model = UserContactModelFactory.ivanEmailCreateModel();
      model.setValue("not-an-email");
      
      assertThatThrownBy(() -> {
        userContactService.createUserContact(model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_CONTACT_VALUE_INVALID
        );
      
      verify(
        userContactRepository,
        never()
      )
        .save(
          any(UserContact.class)
        );
    }
    
  }
  //endregion Create
  
  //region Update
  @Nested
  class Update {
    
    @Test
    public void updatePhoneAllDataSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserContact oldUserContact = UserContactModelFactory.ivanPhoneEntity();
      oldUserContact.setId(id);
      
      UserContact newUserContact = UserContactModelFactory.alexandraPhoneEntity();
      newUserContact.setId(id);
      newUserContact.setUserProfile(
        oldUserContact.getUserProfile()
      );
      
      UserContactUpdateModel model = UserContactModelFactory.alexandraPhoneUpdateModel();
      model.setId(id);
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          oldUserContact.getUserProfile()
            .getId(),
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(newUserContact);
      
      doReturn(oldUserContact)
        .when(userContactService)
        .getUserContactById(id);
      
      UserContact result = userContactService.updateUserContact(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(
          newUserContact.getId()
        );
      
      assertThat(
        result.getUserProfile()
          .getId()
      )
        .isEqualTo(
          oldUserContact.getUserProfile()
            .getId()
        );
      
      assertThat(
        result.getType()
      )
        .isEqualTo(
          model.getType()
        );
      
      assertThat(
        result.getValue()
      )
        .isEqualTo(
          model.getValue()
        );
      
      assertThat(
        result.getIsPrimary()
      )
        .isEqualTo(
          model.getIsPrimary()
        );
      
      assertThat(
        result.getComment()
      )
        .isEqualTo(
          model.getComment()
        );
      
      verify(userContactRepository)
        .existsByUserProfileIdAndTypeAndValue(
          oldUserContact.getUserProfile()
            .getId(),
          model.getType(),
          model.getValue()
        );
      
      verify(userContactRepository)
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    public void updateEmailAllDataSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserContact oldUserContact = UserContactModelFactory.ivanEmailEntity();
      oldUserContact.setId(id);
      
      UserContact newUserContact = UserContactModelFactory.alexandraEmailEntity();
      newUserContact.setId(id);
      newUserContact.setUserProfile(
        oldUserContact.getUserProfile()
      );
      
      UserContactUpdateModel model = UserContactModelFactory.alexandraEmailUpdateModel();
      model.setId(id);
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          oldUserContact.getUserProfile()
            .getId(),
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(newUserContact);
      
      doReturn(oldUserContact)
        .when(userContactService)
        .getUserContactById(id);
      
      UserContact result = userContactService.updateUserContact(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(
          newUserContact.getId()
        );
      
      assertThat(
        result.getUserProfile()
          .getId()
      )
        .isEqualTo(
          oldUserContact.getUserProfile()
            .getId()
        );
      
      assertThat(
        result.getType()
      )
        .isEqualTo(
          model.getType()
        );
      
      assertThat(
        result.getValue()
      )
        .isEqualTo(
          model.getValue()
        );
      
      assertThat(
        result.getIsPrimary()
      )
        .isEqualTo(
          model.getIsPrimary()
        );
      
      assertThat(
        result.getComment()
      )
        .isEqualTo(
          model.getComment()
        );
      
      verify(userContactRepository)
        .existsByUserProfileIdAndTypeAndValue(
          oldUserContact.getUserProfile()
            .getId(),
          model.getType(),
          model.getValue()
        );
      
      verify(userContactRepository)
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    public void updatePhoneExcludeValueSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserContact oldUserContact = UserContactModelFactory.ivanPhoneEntity();
      oldUserContact.setId(id);
      
      UserContact newUserContact = UserContactModelFactory.ivanPhoneEntity();
      newUserContact.setId(id);
      newUserContact.setUserProfile(
        oldUserContact.getUserProfile()
      );
      newUserContact.setComment(UserContactModelFactory.COMMENT_DECREASE_VALUE);
      
      UserContactUpdateModel model = UserContactModelFactory.ivanPhoneUpdateModel();
      model.setId(id);
      model.setComment(UserContactModelFactory.COMMENT_DECREASE_VALUE);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(newUserContact);
      
      doReturn(oldUserContact)
        .when(userContactService)
        .getUserContactById(id);
      
      UserContact result = userContactService.updateUserContact(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
      
      assertThat(
        result.getValue()
      )
        .isEqualTo(
          oldUserContact.getValue()
        );
      
      assertThat(
        result.getComment()
      )
        .isEqualTo(
          model.getComment()
        );
      
      verify(
        userContactRepository,
        never()
      )
        .existsByUserProfileIdAndTypeAndValue(
          any(UUID.class),
          any(ContactType.class),
          any(String.class)
        );
      
      verify(userContactRepository)
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    public void updateEmailExcludeValueSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserContact oldUserContact = UserContactModelFactory.ivanEmailEntity();
      oldUserContact.setId(id);
      
      UserContact newUserContact = UserContactModelFactory.ivanEmailEntity();
      newUserContact.setId(id);
      newUserContact.setUserProfile(
        oldUserContact.getUserProfile()
      );
      newUserContact.setComment(UserContactModelFactory.COMMENT_DECREASE_VALUE);
      
      UserContactUpdateModel model = UserContactModelFactory.ivanEmailUpdateModel();
      model.setId(id);
      model.setComment(UserContactModelFactory.COMMENT_DECREASE_VALUE);
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(newUserContact);
      
      doReturn(oldUserContact)
        .when(userContactService)
        .getUserContactById(id);
      
      UserContact result = userContactService.updateUserContact(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
      
      assertThat(
        result.getValue()
      )
        .isEqualTo(
          oldUserContact.getValue()
        );
      
      assertThat(
        result.getComment()
      )
        .isEqualTo(
          model.getComment()
        );
      
      verify(
        userContactRepository,
        never()
      )
        .existsByUserProfileIdAndTypeAndValue(
          any(UUID.class),
          any(ContactType.class),
          any(String.class)
        );
      
      verify(userContactRepository)
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    public void updatePrimaryDemotesOldPrimaryBeforeSave() {
      UUID id = UUID.randomUUID();
      
      UserContact oldUserContact = UserContactModelFactory.ivanPhoneEntity();
      oldUserContact.setId(id);
      
      UserContact newUserContact = UserContactModelFactory.alexandraPhoneEntity();
      newUserContact.setId(id);
      newUserContact.setUserProfile(
        oldUserContact.getUserProfile()
      );
      newUserContact.setIsPrimary(true);
      
      UserContact oldPrimaryUserContact = UserContactModelFactory.ivanPhoneEntity();
      oldPrimaryUserContact.setUserProfile(
        oldUserContact.getUserProfile()
      );
      oldPrimaryUserContact.setIsPrimary(true);
      
      UserContactUpdateModel model = UserContactModelFactory.alexandraPhoneUpdateModel();
      model.setId(id);
      model.setIsPrimary(true);
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          oldUserContact.getUserProfile()
            .getId(),
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(false);
      
      when(
        userContactRepository.findByUserProfileIdAndTypeAndIsPrimaryTrue(
          oldUserContact.getUserProfile()
            .getId(),
          model.getType()
        )
      )
        .thenReturn(
          Optional.of(oldPrimaryUserContact)
        );
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(newUserContact);
      
      doReturn(oldUserContact)
        .when(userContactService)
        .getUserContactById(id);
      
      UserContact result = userContactService.updateUserContact(id, model);
      
      assertThat(
        result.getIsPrimary()
      )
        .isTrue();
      
      assertThat(
        oldPrimaryUserContact.getIsPrimary()
      )
        .isFalse();
      
      InOrder inOrder = inOrder(userContactRepository);
      
      inOrder.verify(userContactRepository)
        .saveAndFlush(oldPrimaryUserContact);
      
      inOrder.verify(userContactRepository)
        .save(oldUserContact);
    }
    
    @Test
    public void updatePrimaryWhenContactIsAlreadyPrimaryDoesNotDemoteItself() {
      UUID id = UUID.randomUUID();
      
      UserContact oldUserContact = UserContactModelFactory.ivanPhoneEntity();
      oldUserContact.setId(id);
      oldUserContact.setIsPrimary(true);
      
      UserContact newUserContact = UserContactModelFactory.ivanPhoneEntity();
      newUserContact.setId(id);
      newUserContact.setUserProfile(
        oldUserContact.getUserProfile()
      );
      newUserContact.setIsPrimary(true);
      
      UserContactUpdateModel model = UserContactModelFactory.ivanPhoneUpdateModel();
      model.setId(id);
      model.setIsPrimary(true);
      
      when(
        userContactRepository.findByUserProfileIdAndTypeAndIsPrimaryTrue(
          oldUserContact.getUserProfile()
            .getId(),
          model.getType()
        )
      )
        .thenReturn(
          Optional.of(oldUserContact)
        );
      
      when(
        userContactRepository.save(
          any(UserContact.class)
        )
      )
        .thenReturn(newUserContact);
      
      doReturn(oldUserContact)
        .when(userContactService)
        .getUserContactById(id);
      
      UserContact result = userContactService.updateUserContact(id, model);
      
      assertThat(
        result.getIsPrimary()
      )
        .isTrue();
      
      verify(
        userContactRepository,
        never()
      )
        .saveAndFlush(
          any(UserContact.class)
        );
    }
    
    @Test
    void userContactAlreadyExistsCommonException() {
      UUID id = UUID.randomUUID();
      
      UserContact userContact = UserContactModelFactory.ivanEmailEntity();
      userContact.setId(id);
      
      UserContactUpdateModel model = UserContactModelFactory.alexandraEmailUpdateModel();
      model.setId(id);
      
      when(
        userContactRepository.existsByUserProfileIdAndTypeAndValue(
          userContact.getUserProfile()
            .getId(),
          model.getType(),
          model.getValue()
        )
      )
        .thenReturn(true);
      
      doReturn(userContact)
        .when(userContactService)
        .getUserContactById(id);
      
      assertThatThrownBy(() -> {
        userContactService.updateUserContact(id, model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_CONTACT_ALREADY_EXISTS
        );
      
      verify(
        userContactRepository,
        never()
      )
        .save(
          any(UserContact.class)
        );
    }
    
    @Test
    void userContactValueInvalidCommonException() {
      UUID id = UUID.randomUUID();
      
      UserContact userContact = UserContactModelFactory.ivanPhoneEntity();
      userContact.setId(id);
      
      UserContactUpdateModel model = UserContactModelFactory.alexandraPhoneUpdateModel();
      model.setId(id);
      model.setValue("abc");
      
      doReturn(userContact)
        .when(userContactService)
        .getUserContactById(id);
      
      assertThatThrownBy(() -> {
        userContactService.updateUserContact(id, model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_CONTACT_VALUE_INVALID
        );
      
      verify(
        userContactRepository,
        never()
      )
        .save(
          any(UserContact.class)
        );
    }
    
  }
  //endregion Update
  
  //region Delete
  @Nested
  class Delete {
    
    @Test
    public void deleteSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserContact userContact = new UserContact();
      
      userContact.setId(id);
      
      doReturn(userContact)
        .when(userContactService)
        .getUserContactById(id);
      
      userContactService.deleteUserContact(id);
      
      verify(
        userContactService
      )
        .getUserContactById(id);
      
      verify(
        userContactRepository
      )
        .delete(userContact);
    }
    
  }
  //endregion Delete
  
  //region Other
  @Nested
  class Other {
    
    @Test
    public void getByIdSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserContact userContact = new UserContact();
      userContact.setId(id);
      
      when(
        userContactRepository.findById(id)
      )
        .thenReturn(
          Optional.of(userContact)
        );
      
      UserContact result = userContactService.getUserContactById(id);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
    }
    
    @Test
    public void userContactNotFoundCommonException() {
      when(
        userContactRepository.findById(
          any(UUID.class)
        )
      )
        .thenReturn(Optional.empty());
      
      assertThatThrownBy(() -> {
        userContactService.getUserContactById(UUID.randomUUID());
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_CONTACT_NOT_FOUND
        );
    }
    
    @Test
    public void getByUserProfileIdSuccessfully() {
      UUID userProfileId = UUID.randomUUID();
      
      UserProfile userProfile = UserProfileModelFactory.ivanEntity();
      userProfile.setId(userProfileId);
      
      UserContact phone = UserContactModelFactory.ivanPhoneEntity();
      phone.setUserProfile(userProfile);
      
      UserContact email = UserContactModelFactory.ivanEmailEntity();
      email.setUserProfile(userProfile);
      
      List<UserContact> userContacts = List.of(phone, email);
      
      when(
        userProfileService.getUserProfileById(
          userProfileId
        )
      )
        .thenReturn(userProfile);
      
      when(
        userContactRepository.findByUserProfileId(
          eq(userProfileId),
          any(Sort.class)
        )
      )
        .thenReturn(userContacts);
      
      List<UserContact> result = userContactService.getUserContactsByUserProfileId(userProfileId);
      
      assertThat(result)
        .isEqualTo(userContacts);
      
      verify(userProfileService)
        .getUserProfileById(userProfileId);
    }
    
  }
  //endregion Other
  
}