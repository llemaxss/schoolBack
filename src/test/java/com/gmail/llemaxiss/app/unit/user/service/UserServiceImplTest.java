package com.gmail.llemaxiss.app.unit.user.service;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app._common.hibernateFilter.component.HibernateFilterManager;
import com.gmail.llemaxiss.app._common.security.model.response.AppUserDetails;
import com.gmail.llemaxiss.app.unit._common.CommonServiceImplTest;
import com.gmail.llemaxiss.app._common.user.modelFactory.UserModelFactory;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.user.model.request.UserCreateModel;
import com.gmail.llemaxiss.app.user.model.request.UserUpdateModel;
import com.gmail.llemaxiss.app.user.repository.UserRepository;
import com.gmail.llemaxiss.app.user.service.UserServiceImpl;
import com.gmail.llemaxiss.app.user.util.UserConstants;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest extends CommonServiceImplTest {
  
  private static final String ENCODED_PASSWORD = "encoded-password";
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private PasswordEncoder passwordEncoder;
  
  @Mock
  private HibernateFilterManager hibernateFilterManager;
  
  @Spy
  @InjectMocks
  private UserServiceImpl userService;
  
  @BeforeEach
  public void setUp() throws Exception {
    setUpEntityManager(userService);
  }
  
  @AfterEach
  public void tearDown() {
    SecurityContextHolder.clearContext();
  }
  
  //region Create
  @Nested
  class Create {
    
    @Test
    public void createSuccessfully() {
      UUID id = UUID.randomUUID();
      
      UserCreateModel model = UserModelFactory.ivanCreateModel();
      
      User savedUser = UserModelFactory.ivanEntity();
      savedUser.setId(id);
      
      when(
        userRepository.existsByUsername(
          model.getUsername()
        )
      )
        .thenReturn(false);
      
      when(
        passwordEncoder.encode(
          model.getPassword()
        )
      )
        .thenReturn(ENCODED_PASSWORD);
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(savedUser);
      
      User result = userService.createUser(model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
      
      assertThat(
        result.getUsername()
      )
        .isEqualTo(
          model.getUsername()
        );
      
      verify(hibernateFilterManager)
        .disableActiveUserFilter();
      
      verify(userRepository)
        .existsByUsername(
          model.getUsername()
        );
      
      verify(passwordEncoder)
        .encode(
          model.getPassword()
        );
      
      verify(userRepository)
        .save(
          any(User.class)
        );
    }
    
    @Test
    public void createNormalizesUsername() {
      UserCreateModel model = UserModelFactory.ivanCreateModel();
      model.setUsername(
        UserModelFactory.denormalizeUsername(
          UserModelFactory.IVAN_USERNAME
        )
      );
      
      User savedUser = UserModelFactory.ivanEntity();
      
      when(
        userRepository.existsByUsername(UserModelFactory.IVAN_USERNAME)
      )
        .thenReturn(false);
      
      when(
        passwordEncoder.encode(
          model.getPassword()
        )
      )
        .thenReturn(ENCODED_PASSWORD);
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(savedUser);
      
      userService.createUser(model);
      
      assertThat(
        model.getUsername()
      )
        .isEqualTo(UserModelFactory.IVAN_USERNAME);
      
      verify(userRepository)
        .existsByUsername(UserModelFactory.IVAN_USERNAME);
      
      verify(userRepository)
        .save(
          any(User.class)
        );
    }
    
    @Test
    void usernameAlreadyExistsCommonException() {
      UserCreateModel model = UserModelFactory.ivanCreateModel();
      
      when(
        userRepository.existsByUsername(
          model.getUsername()
        )
      )
        .thenReturn(true);
      
      assertThatThrownBy(() -> {
        userService.createUser(model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_USERNAME_ALREADY_EXISTS
        );
      
      verify(
        passwordEncoder,
        never()
      )
        .encode(
          anyString()
        );
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
        );
    }
    
    @Test
    void usernameAlreadyExistsIgnoringCaseCommonException() {
      UserCreateModel model = UserModelFactory.ivanCreateModel();
      model.setUsername(
        UserModelFactory.IVAN_USERNAME.toUpperCase(Locale.ROOT)
      );
      
      when(
        userRepository.existsByUsername(
          UserModelFactory.IVAN_USERNAME
        )
      )
        .thenReturn(true);
      
      assertThatThrownBy(() -> {
        userService.createUser(model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_USERNAME_ALREADY_EXISTS
        );
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
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
      
      User oldUser = UserModelFactory.ivanEntity();
      oldUser.setId(id);
      
      User newUser = UserModelFactory.alexandraEntity();
      newUser.setId(id);
      
      UserUpdateModel model = UserModelFactory.alexandraUpdateModel();
      model.setId(id);
      
      when(
        userRepository.existsByUsername(
          model.getUsername()
        )
      )
        .thenReturn(false);
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(newUser);
      
      doReturn(oldUser)
        .when(userService)
        .getUserById(id);
      
      User result = userService.updateUser(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
      
      assertThat(
        result.getUsername()
      )
        .isEqualTo(
          model.getUsername()
        );
      
      verify(hibernateFilterManager)
        .disableActiveUserFilter();
      
      verify(userRepository)
        .existsByUsername(
          model.getUsername()
        );
      
      verify(userRepository)
        .save(
          any(User.class)
        );
    }
    
    @Test
    public void updateNormalizesUsername() {
      UUID id = UUID.randomUUID();
      
      User oldUser = UserModelFactory.ivanEntity();
      oldUser.setId(id);
      
      User newUser = UserModelFactory.alexandraEntity();
      newUser.setId(id);
      
      UserUpdateModel model = UserModelFactory.alexandraUpdateModel();
      model.setId(id);
      model.setUsername(
        UserModelFactory.denormalizeUsername(
          UserModelFactory.ALEXANDRA_USERNAME
        )
      );
      
      when(
        userRepository.existsByUsername(
          UserModelFactory.ALEXANDRA_USERNAME
        )
      )
        .thenReturn(false);
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(newUser);
      
      doReturn(oldUser)
        .when(userService)
        .getUserById(id);
      
      userService.updateUser(id, model);
      
      assertThat(
        oldUser.getUsername()
      )
        .isEqualTo(
          UserModelFactory.ALEXANDRA_USERNAME
        );
      
      verify(userRepository)
        .existsByUsername(
          UserModelFactory.ALEXANDRA_USERNAME
        );
    }
    
    @Test
    public void updateExcludeUsernameSuccessfully() {
      UUID id = UUID.randomUUID();
      
      User oldUser = UserModelFactory.ivanEntity();
      oldUser.setId(id);
      
      UserUpdateModel model = UserModelFactory.ivanUpdateModel();
      model.setId(id);
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(oldUser);
      
      doReturn(oldUser)
        .when(userService)
        .getUserById(id);
      
      User result = userService.updateUser(id, model);
      
      assertThat(
        result.getUsername()
      )
        .isEqualTo(
          UserModelFactory.IVAN_USERNAME
        );
      
      verify(
        userRepository,
        never()
      )
        .existsByUsername(
          anyString()
        );
      
      verify(userRepository)
        .save(
          any(User.class)
        );
    }
    
    @Test
    public void updateSameUsernameInOtherCaseDoesNotCheckExists() {
      UUID id = UUID.randomUUID();
      
      User oldUser = UserModelFactory.ivanEntity();
      oldUser.setId(id);
      
      UserUpdateModel model = UserModelFactory.ivanUpdateModel();
      model.setId(id);
      model.setUsername(
        UserModelFactory.IVAN_USERNAME.toUpperCase(Locale.ROOT)
      );
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(oldUser);
      
      doReturn(oldUser)
        .when(userService)
        .getUserById(id);
      
      userService.updateUser(id, model);
      
      assertThat(
        oldUser.getUsername()
      )
        .isEqualTo(
          UserModelFactory.IVAN_USERNAME
        );
      
      verify(
        userRepository,
        never()
      )
        .existsByUsername(
          anyString()
        );
    }
    
    @Test
    void usernameAlreadyExistsCommonException() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      UserUpdateModel model = UserModelFactory.alexandraUpdateModel();
      model.setId(id);
      
      when(
        userRepository.existsByUsername(
          model.getUsername()
        )
      )
        .thenReturn(true);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      assertThatThrownBy(() -> {
        userService.updateUser(id, model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_USERNAME_ALREADY_EXISTS
        );
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
        );
    }
    
    @Test
    void protectedUserCommonException() {
      UUID id = UserConstants.ADMIN_USER_ID;
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      UserUpdateModel model = UserModelFactory.alexandraUpdateModel();
      model.setId(id);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      assertThatThrownBy(() -> {
        userService.updateUser(id, model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_PROTECTED
        );
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
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
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      authenticateAs(
        UserModelFactory.alexandraEntity()
      );
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      userService.deleteUser(id);
      
      verify(hibernateFilterManager)
        .disableActiveUserFilter();
      
      verify(userService)
        .getUserById(id);
      
      verify(userRepository)
        .delete(user);
    }
    
    @Test
    void protectedUserCommonException() {
      UUID id = UserConstants.SYSTEM_USER_ID;
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      authenticateAs(
        UserModelFactory.alexandraEntity()
      );
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      assertThatThrownBy(() -> {
        userService.deleteUser(id);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_PROTECTED
        );
      
      verify(
        userRepository,
        never()
      )
        .delete(
          any(User.class)
        );
    }
    
    @Test
    void deleteHimselfCommonException() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      authenticateAs(user);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      assertThatThrownBy(() -> {
        userService.deleteUser(id);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_SELF_ACTION_FORBIDDEN
        );
      
      verify(
        userRepository,
        never()
      )
        .delete(
          any(User.class)
        );
    }
    
  }
  //endregion Delete
  
  //region Activity
  @Nested
  class Activity {
    
    @Test
    public void activateSuccessfully() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      user.setIsActive(false);
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(user);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      User result = userService.activateUser(id);
      
      assertThat(
        result.getIsActive()
      )
        .isTrue();
      
      verify(hibernateFilterManager)
        .disableActiveUserFilter();
      
      verify(userRepository)
        .save(user);
    }
    
    @Test
    public void activateAlreadyActiveDoesNotSave() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      User result = userService.activateUser(id);
      
      assertThat(
        result.getIsActive()
      )
        .isTrue();
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
        );
    }
    
    @Test
    public void deactivateSuccessfully() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      authenticateAs(
        UserModelFactory.alexandraEntity()
      );
      
      when(
        userRepository.save(
          any(User.class)
        )
      )
        .thenReturn(user);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      User result = userService.deactivateUser(id);
      
      assertThat(
        result.getIsActive()
      )
        .isFalse();
      
      verify(hibernateFilterManager)
        .disableActiveUserFilter();
      
      verify(userRepository)
        .save(user);
    }
    
    @Test
    public void deactivateAlreadyInactiveDoesNotSave() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      user.setIsActive(false);
      
      authenticateAs(
        UserModelFactory.alexandraEntity()
      );
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      User result = userService.deactivateUser(id);
      
      assertThat(
        result.getIsActive()
      )
        .isFalse();
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
        );
    }
    
    @Test
    void deactivateProtectedUserCommonException() {
      UUID id = UserConstants.ADMIN_USER_ID;
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      authenticateAs(
        UserModelFactory.alexandraEntity()
      );
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      assertThatThrownBy(() -> {
        userService.deactivateUser(id);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_PROTECTED
        );
      
      assertThat(
        user.getIsActive()
      )
        .isTrue();
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
        );
    }
    
    @Test
    void deactivateHimselfCommonException() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      authenticateAs(user);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      assertThatThrownBy(() -> {
        userService.deactivateUser(id);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_SELF_ACTION_FORBIDDEN
        );
      
      assertThat(
        user.getIsActive()
      )
        .isTrue();
      
      verify(
        userRepository,
        never()
      )
        .save(
          any(User.class)
        );
    }
    
  }
  //endregion Activity
  
  //region Other
  @Nested
  class Other {
    
    @Test
    public void getByIdSuccessfully() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      
      when(
        userRepository.findById(id)
      )
        .thenReturn(
          Optional.of(user)
        );
      
      User result = userService.getUserById(id);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
    }
    
    @Test
    public void userNotFoundByIdCommonException() {
      when(
        userRepository.findById(
          any(UUID.class)
        )
      )
        .thenReturn(Optional.empty());
      
      assertThatThrownBy(() -> {
        userService.getUserById(UUID.randomUUID());
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_NOT_FOUND
        );
    }
    
    @Test
    public void getByIdIncludingInactiveSuccessfully() {
      UUID id = UUID.randomUUID();
      
      User user = UserModelFactory.ivanEntity();
      user.setId(id);
      user.setIsActive(false);
      
      doReturn(user)
        .when(userService)
        .getUserById(id);
      
      User result = userService.getUserByIdIncludingInactive(id);
      
      assertThat(result)
        .isEqualTo(user);
      
      verify(hibernateFilterManager)
        .disableActiveUserFilter();
    }
    
    @Test
    public void getByUsernameSuccessfully() {
      User user = UserModelFactory.ivanEntity();
      
      when(
        userRepository.findByUsername(
          UserModelFactory.IVAN_USERNAME
        )
      )
        .thenReturn(user);
      
      User result = userService.getUserByUsername(
        UserModelFactory.IVAN_USERNAME
      );
      
      assertThat(result)
        .isEqualTo(user);
    }
    
    @Test
    public void getByUsernameNormalizesUsername() {
      User user = UserModelFactory.ivanEntity();
      
      when(
        userRepository.findByUsername(
          UserModelFactory.IVAN_USERNAME
        )
      )
        .thenReturn(user);
      
      User result = userService.getUserByUsername(
        UserModelFactory.denormalizeUsername(
          UserModelFactory.IVAN_USERNAME
        )
      );
      
      assertThat(result)
        .isEqualTo(user);
      
      verify(userRepository)
        .findByUsername(
          UserModelFactory.IVAN_USERNAME
        );
    }
    
    @Test
    public void userNotFoundByUsernameCommonException() {
      when(
        userRepository.findByUsername(
          anyString()
        )
      )
        .thenReturn(null);
      
      assertThatThrownBy(() -> {
        userService.getUserByUsername(
          UserModelFactory.IVAN_USERNAME
        );
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.USER_NOT_FOUND
        );
    }
    
    @Test
    public void getCurrentUserSuccessfully() {
      User currentUser = UserModelFactory.ivanEntity();
      
      authenticateAs(currentUser);
      
      doReturn(currentUser)
        .when(userService)
        .getUserById(
          currentUser.getId()
        );
      
      User result = userService.getCurrentUser();
      
      assertThat(result)
        .isEqualTo(currentUser);
    }
    
    @Test
    public void loadUserByUsernameSuccessfully() {
      User user = UserModelFactory.ivanEntity();
      
      when(
        userRepository.findByUsername(
          UserModelFactory.IVAN_USERNAME
        )
      )
        .thenReturn(user);
      
      UserDetails result = userService.loadUserByUsername(
        UserModelFactory.denormalizeUsername(
          UserModelFactory.IVAN_USERNAME
        )
      );
      
      assertThat(
        result.getUsername()
      )
        .isEqualTo(
          UserModelFactory.IVAN_USERNAME
        );
      
      verify(userRepository)
        .findByUsername(
          UserModelFactory.IVAN_USERNAME
        );
    }
    
    @Test
    public void loadUserByUsernameNotFoundUsernameNotFoundException() {
      when(
        userRepository.findByUsername(
          anyString()
        )
      )
        .thenReturn(null);
      
      assertThatThrownBy(() -> {
        userService.loadUserByUsername(
          UserModelFactory.IVAN_USERNAME
        );
      })
        .isInstanceOf(UsernameNotFoundException.class);
    }
    
  }
  //endregion Other
  
  private void authenticateAs(User user) {
    AppUserDetails appUserDetails = new AppUserDetails(user);
    
    SecurityContextHolder.getContext()
      .setAuthentication(
        new UsernamePasswordAuthenticationToken(
          appUserDetails,
          null,
          List.of()
        )
      );
  }
}