package com.gmail.llemaxiss.app.unit.userProfile.service;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;

import com.gmail.llemaxiss.app.unit._common.CommonServiceImplTest;
import com.gmail.llemaxiss.app.unit.userProfile.modelFactory.UserProfileModelFactory;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileCreateModel;
import com.gmail.llemaxiss.app.userProfile.model.request.UserProfileUpdateModel;
import com.gmail.llemaxiss.app.userProfile.repository.UserProfileRepository;
import com.gmail.llemaxiss.app.userProfile.service.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceImplTest extends CommonServiceImplTest {
  
  @Mock
  private UserProfileRepository userProfileRepository;
  
  @Spy
  @InjectMocks
  private UserProfileServiceImpl userProfileService;
  
  @BeforeEach
  public void setUp() throws Exception {
    setUpEntityManager(userProfileService);
  }
  
  //region Create
  @Nested
  class Create {
    
    @Test
    public void createSuccessfully() {
      UUID userId = UUID.randomUUID();
      
      UserProfileCreateModel model = UserProfileModelFactory.ivanCreateModel();
      model.getUser()
        .setId(userId);
      
      UserProfile savedUserProfile = UserProfileModelFactory.ivanEntity();
      savedUserProfile.getUser()
        .setId(userId);
      
      when(
        userProfileRepository.existsByUserId(userId)
      )
        .thenReturn(false);
      
      when(
        em.getReference(User.class, userId)
      )
        .thenReturn(
          savedUserProfile.getUser()
        );
      
      when(
        userProfileRepository.save(
          any(UserProfile.class)
        )
      )
        .thenReturn(savedUserProfile);
      
      UserProfile result = userProfileService.createUserProfile(model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(result.getId())
        .isEqualTo(savedUserProfile.getId());
      
      assertThat(
        result.getUser()
          .getId()
      ).isEqualTo(userId);
      
      assertThat(
        result.getFirstName()
      )
        .isEqualTo(
          model.getFirstName()
        );
      
      assertThat(
        result.getLastName()
      )
        .isEqualTo(
          model.getLastName()
        );
      
      assertThat(
        result.getMiddleName()
      )
        .isEqualTo(
          model.getMiddleName()
        );
      
      assertThat(
        result.getDateOfBirth()
      )
        .isEqualTo(
          model.getDateOfBirth()
        );
      
      assertThat(
        result.getGender()
      )
        .isEqualTo(
          model.getGender()
        );
      
      verify(userProfileRepository)
        .existsByUserId(userId);
      
      verify(userProfileRepository)
        .save(
          any(UserProfile.class)
        );
    }
    
    @Test
    void userProfileAlreadyExistsCommonException() {
      UserProfileCreateModel model = UserProfileModelFactory.ivanCreateModel();
      
      when(
        userProfileRepository.existsByUserId(
          model.getUser()
            .getId()
        )
      )
        .thenReturn(true);
      
      assertThatThrownBy(() -> {
        userProfileService.createUserProfile(model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_PROFILE_ALREADY_EXISTS
        );
      
      verify(
        userProfileRepository,
        never()
      )
        .save(
          any(UserProfile.class)
        );
    }
    
  }
  //endregion Create
  
  //region Update
  @Nested
  class Update {
    
    @Test
    public void updateAllDataSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserProfile oldUserProfile = UserProfileModelFactory.ivanEntity();
      oldUserProfile.setId(id);
      
      UserProfile newUserProfile = UserProfileModelFactory.alexandraEntity();
      newUserProfile.setId(id);
      
      UserProfileUpdateModel model = UserProfileModelFactory.alexandraUpdateModel();
      model.setId(id);
      model.getUser()
        .setId(
          newUserProfile.getUser()
            .getId()
        );
      
      when(
        userProfileRepository.existsByUserId(
          model.getUser()
            .getId()
        )
      )
        .thenReturn(false);
      
      when(
        userProfileRepository.save(
          any(UserProfile.class)
        )
      )
        .thenReturn(newUserProfile);
      
      doReturn(oldUserProfile)
        .when(userProfileService)
        .getUserProfileById(id);
      
      UserProfile result = userProfileService.updateUserProfile(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(
          newUserProfile.getId()
        );
      
      assertThat(
        result.getUser()
          .getId()
      )
        .isEqualTo(
          model.getUser()
            .getId()
        );
      
      assertThat(
        result.getFirstName()
      )
        .isEqualTo(
          model.getFirstName()
        );
      
      assertThat(
        result.getLastName()
      )
        .isEqualTo(
          model.getLastName()
        );
      
      assertThat(
        result.getMiddleName()
      )
        .isEqualTo(
          model.getMiddleName()
        );
      
      assertThat(
        result.getDateOfBirth()
      )
        .isEqualTo(
          model.getDateOfBirth()
        );
      
      assertThat(
        result.getGender()
      )
        .isEqualTo(
          model.getGender()
        );
      
      verify(userProfileRepository)
        .existsByUserId(
          model.getUser()
            .getId()
        );
      
      verify(userProfileRepository)
        .save(
          any(UserProfile.class)
        );
    }
    
    @Test
    public void updateExcludeUserSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserProfile oldUserProfile = UserProfileModelFactory.ivanEntity();
      oldUserProfile.setId(id);
      
      UserProfile newUserProfile = UserProfileModelFactory.alexandraEntity();
      newUserProfile.setId(id);
      newUserProfile.getUser()
        .setId(
          oldUserProfile.getUser()
            .getId()
        );
      
      UserProfileUpdateModel model = UserProfileModelFactory.alexandraUpdateModel();
      model.setId(id);
      model.getUser()
        .setId(
          oldUserProfile.getUser()
            .getId()
        );
      
      when(
        em.getReference(
          User.class,
          oldUserProfile.getUser()
            .getId()
        )
      )
        .thenReturn(
          oldUserProfile.getUser()
        );
      
      when(
        userProfileRepository.save(
          any(UserProfile.class)
        )
      )
        .thenReturn(newUserProfile);
      
      doReturn(oldUserProfile)
        .when(userProfileService)
        .getUserProfileById(id);
      
      UserProfile result = userProfileService.updateUserProfile(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
      
      assertThat(
        result.getUser()
          .getId()
      ).isEqualTo(
        oldUserProfile.getUser()
          .getId()
      );
      
      assertThat(
        result.getFirstName()
      )
        .isEqualTo(
          model.getFirstName()
        );
      
      assertThat(
        result.getLastName()
      )
        .isEqualTo(
          model.getLastName()
        );
      
      assertThat(
        result.getMiddleName()
      )
        .isEqualTo(
          model.getMiddleName()
        );
      
      assertThat(
        result.getDateOfBirth()
      )
        .isEqualTo(
          model.getDateOfBirth()
        );
      
      assertThat(
        result.getGender()
      )
        .isEqualTo(
          model.getGender()
        );
      
      verify(
        userProfileRepository,
        never()
      )
        .existsByUserId(
          any(UUID.class)
        );
      
      verify(userProfileRepository)
        .save(
          any(UserProfile.class)
        );
    }
    
    @Test
    void userProfileAlreadyExistsCommonException() {
      UUID id = UUID.randomUUID();
      
      UserProfile userProfile = UserProfileModelFactory.ivanEntity();
      userProfile.setId(id);
      userProfile.getUser()
        .setId(UUID.randomUUID());
      
      UserProfileUpdateModel model = UserProfileModelFactory.ivanUpdateModel();
      model.setId(id);
      
      when(
        userProfileRepository.existsByUserId(
          model.getUser()
            .getId()
        )
      )
        .thenReturn(true);
      
      doReturn(userProfile)
        .when(userProfileService)
        .getUserProfileById(id);
      
      assertThatThrownBy(() -> {
        userProfileService.updateUserProfile(id, model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_PROFILE_ALREADY_EXISTS
        );
      
      verify(
        userProfileRepository,
        never()
      )
        .save(
          any(UserProfile.class)
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
      
      UserProfile userProfile = new UserProfile();
      
      userProfile.setId(id);
      
      doReturn(userProfile)
        .when(userProfileService)
        .getUserProfileById(id);
      
      userProfileService.deleteUserProfile(id);
      
      verify(
        userProfileService
      )
        .getUserProfileById(id);
      
      verify(
        userProfileRepository
      )
        .delete(userProfile);
    }
    
  }
  //endregion Delete
  
  //region Other
  @Nested
  class Other {
    
    @Test
    public void getByIdSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserProfile userProfile = new UserProfile();
      userProfile.setId(id);
      
      when(
        userProfileRepository.findById(id)
      )
        .thenReturn(
          Optional.of(userProfile)
        );
      
      UserProfile result = userProfileService.getUserProfileById(id);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
    }
    
    @Test
    public void userProfileNotFoundCommonException() {
      when(
        userProfileRepository.findById(
          any(UUID.class)
        )
      )
        .thenReturn(Optional.empty());
      
      assertThatThrownBy(() -> {
        userProfileService.getUserProfileById(UUID.randomUUID());
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_PROFILE_NOT_FOUND
        );
    }
    
  }
  //endregion Other
  
}
