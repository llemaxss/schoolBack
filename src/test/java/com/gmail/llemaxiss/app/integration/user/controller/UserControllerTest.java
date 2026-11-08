package com.gmail.llemaxiss.app.integration.user.controller;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.security.model.request.LoginRequest;
import com.gmail.llemaxiss.app._common.security.util.SecurityUtil;
import com.gmail.llemaxiss.app.auth.controller.AuthController;
import com.gmail.llemaxiss.app.integration._common.AbstractIntegrationTest;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app._common.user.modelFactory.UserModelFactory;
import com.gmail.llemaxiss.app.user.controller.UserController;
import com.gmail.llemaxiss.app.user.model.request.UserCreateModel;
import com.gmail.llemaxiss.app.user.model.request.UserUpdateModel;
import com.gmail.llemaxiss.app.user.util.UserConstants;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractIntegrationTest {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  //region Create
  @Nested
  class Create {
    
    @Test
    public void createSuccessfully() throws Exception {
      UserCreateModel createModel = UserModelFactory.ivanCreateModel();
      
      mockMvc.perform(
        post(UserController.URL)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(createModel)
          )
      )
        .andExpect(
          status()
            .isCreated()
        )
        .andExpect(
          jsonPath("$.id")
            .value(
              not(
                emptyOrNullString()
              )
            )
        )
        .andExpect(
          jsonPath("$.username")
            .value(
              UserModelFactory.IVAN_USERNAME
            )
        )
        .andExpect(
          jsonPath("$.isActive")
            .value(true)
        )
        .andExpect(
          jsonPath("$.password")
            .doesNotExist()
        );
      
      String password = jdbcTemplate.queryForObject(
        "SELECT password FROM school_user WHERE username = ?",
        String.class,
        UserModelFactory.IVAN_USERNAME
      );
      
      String createdBy = jdbcTemplate.queryForObject(
        "SELECT created_by FROM school_user WHERE username = ?",
        String.class,
        UserModelFactory.IVAN_USERNAME
      );
      
      assertThat(password)
        .startsWith("$2")
        .isNotEqualTo(
          UserModelFactory.IVAN_PASSWORD
        );
      
      assertThat(createdBy)
        .isEqualTo(
          adminUsername()
        );
    }
    
    @Test
    public void createNormalizesUsernameAndUserCanLoginInAnyCase() throws Exception {
      UserCreateModel createModel = UserModelFactory.ivanCreateModel();
      
      createModel.setUsername(
        UserModelFactory.denormalizeUsername(
          createModel.getUsername()
        )
      );
      
      mockMvc.perform(
        post(UserController.URL)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(createModel)
          )
      )
        .andExpect(
          status()
            .isCreated()
        )
        .andExpect(
          jsonPath("$.username")
            .value(UserModelFactory.IVAN_USERNAME)
        );
      
      login(
        UserModelFactory.denormalizeUsername(
          UserModelFactory.IVAN_USERNAME
        ),
        UserModelFactory.IVAN_PASSWORD
      )
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.username")
            .value(
              UserModelFactory.IVAN_USERNAME
            )
        )
        .andExpect(
          jsonPath("$.token")
            .value(
              not(
                emptyOrNullString()
              )
            )
        );
    }
    
    @Test
    public void usernameAlreadyExistsBadRequest() throws Exception {
      createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      UserCreateModel createModel = UserModelFactory.createModel(
        UserModelFactory.denormalizeUsername(
          UserModelFactory.IVAN_USERNAME
        ),
        UserModelFactory.ALEXANDRA_PASSWORD
      );
      
      mockMvc.perform(
        post(UserController.URL)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(createModel)
          )
      )
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_USERNAME_ALREADY_EXISTS.getId()
            )
        );
    }
    
    @Test
    public void usernameOfInactiveUserAlreadyExistsBadRequest() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      UserCreateModel createModel = UserModelFactory.createModel(
        UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD
      );
      
      deactivate(id)
        .andExpect(
          status()
            .isOk()
        );
      
      mockMvc.perform(
        post(UserController.URL)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(createModel)
          )
      )
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_USERNAME_ALREADY_EXISTS.getId()
            )
        );
    }
    
    @Test
    public void blankUsernameValidationFailed() throws Exception {
      UserCreateModel createModel = UserModelFactory.createModel(
        "   ",
        UserModelFactory.IVAN_PASSWORD
      );
      
      mockMvc.perform(
        post(UserController.URL)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(createModel)
          )
      )
        .andExpect(status().isBadRequest())
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.VALIDATION_FAILED.getId()
            )
        )
        .andExpect(
          jsonPath("$.fieldErrors[0].field")
            .value("username")
        );
    }
    
  }
  //endregion Create
  
  //region Get
  @Nested
  class Get {
    
    @Test
    public void getByIdSuccessfully() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      mockMvc.perform(
        get(UserController.URL + "/" + id)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.id")
            .value(id.toString())
        )
        .andExpect(
          jsonPath("$.username")
            .value(
              UserModelFactory.IVAN_USERNAME
            )
        )
        .andExpect(
          jsonPath("$.isActive")
            .value(true)
        )
        .andExpect(
          jsonPath("$.password")
            .doesNotExist()
        );
    }
    
    @Test
    public void getInactiveUserByIdSuccessfully() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      deactivate(id)
        .andExpect(
          status()
            .isOk()
        );
      
      mockMvc.perform(
        get(UserController.URL + "/" + id)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.username")
            .value(UserModelFactory.IVAN_USERNAME)
        )
        .andExpect(
          jsonPath("$.isActive")
            .value(false)
        );
    }
    
    @Test
    public void userNotFoundBadRequest() throws Exception {
      mockMvc.perform(
        get(UserController.URL + "/" + UUID.randomUUID())
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_NOT_FOUND.getId()
            )
        );
    }
    
  }
  //endregion Get
  
  //region Update
  @Nested
  class Update {
    
    @Test
    public void updateUsernameSuccessfully() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      UserUpdateModel updateModel = UserModelFactory.updateModel(
        id,
        UserModelFactory.denormalizeUsername(
          UserModelFactory.ALEXANDRA_USERNAME
        )
      );
      
      mockMvc.perform(
        put(UserController.URL + "/" + id)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(updateModel)
          )
      )
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.id")
            .value(id.toString())
        )
        .andExpect(
          jsonPath("$.username")
            .value(
              UserModelFactory.ALEXANDRA_USERNAME
            )
        );
      
      String updatedBy = jdbcTemplate.queryForObject(
        "SELECT updated_by FROM school_user WHERE id = ?",
        String.class,
        id
      );
      
      assertThat(updatedBy)
        .isEqualTo(
          adminUsername()
        );
      
      login(UserModelFactory.ALEXANDRA_USERNAME, UserModelFactory.IVAN_PASSWORD)
        .andExpect(
          status()
            .isOk()
        );
    }
    
    @Test
    public void usernameAlreadyExistsBadRequest() throws Exception {
      UUID ivanId = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      createUser(UserModelFactory.ALEXANDRA_USERNAME, UserModelFactory.ALEXANDRA_PASSWORD);
      
      UserUpdateModel updateModel = UserModelFactory.updateModel(ivanId, UserModelFactory.ALEXANDRA_USERNAME);
      
      mockMvc.perform(
        put(UserController.URL + "/" + ivanId)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(updateModel)
          )
      )
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_USERNAME_ALREADY_EXISTS.getId()
            )
        );
    }
    
    @Test
    public void protectedUserBadRequest() throws Exception {
      UserUpdateModel updateModel = UserModelFactory.updateModel(
        UserConstants.ADMIN_USER_ID, UserModelFactory.ALEXANDRA_USERNAME
      );
      
      mockMvc.perform(
        put(UserController.URL + "/" + UserConstants.ADMIN_USER_ID)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(updateModel)
          )
      )
        .andExpect(status().isBadRequest())
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_PROTECTED.getId()
            )
        );
    }
    
  }
  //endregion Update
  
  //region Delete
  @Nested
  class Delete {
    
    @Test
    public void deleteSuccessfully() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      mockMvc.perform(
        delete(UserController.URL + "/" + id)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(
          status()
            .isNoContent()
        );
      
      Boolean isDeleted = jdbcTemplate.queryForObject(
        "SELECT delete_ts IS NOT NULL FROM school_user WHERE id = ?",
        Boolean.class,
        id
      );
      
      String deletedBy = jdbcTemplate.queryForObject(
        "SELECT deleted_by FROM school_user WHERE id = ?",
        String.class,
        id
      );
      
      assertThat(isDeleted)
        .isTrue();
      
      assertThat(deletedBy)
        .isEqualTo("admin");
      
      mockMvc.perform(
        get(UserController.URL + "/" + id)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(status().isBadRequest())
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_NOT_FOUND.getId()
            )
        );
    }
    
    @Test
    public void usernameCanBeReusedAfterDelete() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      mockMvc.perform(
        delete(UserController.URL + "/" + id)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(
          status()
            .isNoContent()
        );
      
      createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
    }
    
    @Test
    public void deleteHimselfBadRequest() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      assignRole(UserModelFactory.IVAN_USERNAME, RoleType.ADMIN);
      
      mockMvc.perform(
        delete(UserController.URL + "/" + id)
          .header(
            HttpHeaders.AUTHORIZATION,
            bearerTokenFor(UserModelFactory.IVAN_USERNAME)
          )
      )
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_SELF_ACTION_FORBIDDEN.getId()
            )
        );
    }
    
    @Test
    public void protectedUserBadRequest() throws Exception {
      mockMvc.perform(
        delete(UserController.URL + "/" + UserConstants.SYSTEM_USER_ID)
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_PROTECTED.getId()
            )
        );
    }
    
  }
  //endregion Delete
  
  //region Activity
  @Nested
  class Activity {
    
    @Test
    public void deactivatedUserCannotLoginAndHisTokenStopsWorking() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      String token = loginAndGetToken(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      mockMvc.perform(
        get(UserController.URL + "/me")
          .header(
            HttpHeaders.AUTHORIZATION,
            SecurityUtil.BEARER_PREFIX + token
          )
      )
        .andExpect(
          status()
            .isOk()
        );
      
      deactivate(id)
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.isActive")
            .value(false)
        );
      
      login(
        UserModelFactory.IVAN_USERNAME,
        UserModelFactory.IVAN_PASSWORD
      )
        .andExpect(
          status()
            .isUnauthorized()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.AUTHENTICATION_FAILED.getId()
            )
        );
      
      mockMvc.perform(
        get(UserController.URL + "/me")
          .header(
            HttpHeaders.AUTHORIZATION,
            SecurityUtil.BEARER_PREFIX + token
          )
      )
        .andExpect(
          status()
            .isUnauthorized()
        );
    }
    
    @Test
    public void activatedUserCanLoginAgain() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      deactivate(id)
        .andExpect(
          status()
            .isOk()
        );
      
      mockMvc.perform(
        post(UserController.URL + "/" + id + "/activate")
          .header(
            HttpHeaders.AUTHORIZATION,
            adminBearerToken()
          )
      )
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.isActive")
            .value(true)
        );
      
      login(
        UserModelFactory.IVAN_USERNAME,
        UserModelFactory.IVAN_PASSWORD
      )
        .andExpect(
          status()
            .isOk()
        );
    }
    
    @Test
    public void deactivateTwiceIsIdempotent() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      deactivate(id)
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.isActive")
            .value(false)
        );
      
      deactivate(id)
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.isActive")
            .value(false)
        );
    }
    
    @Test
    public void deactivateHimselfBadRequest() throws Exception {
      UUID id = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      
      assignRole(UserModelFactory.IVAN_USERNAME, RoleType.ADMIN);
      
      mockMvc.perform(
        post(UserController.URL + "/" + id + "/deactivate")
          .header(
            HttpHeaders.AUTHORIZATION,
            bearerTokenFor(UserModelFactory.IVAN_USERNAME)
          )
      )
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_SELF_ACTION_FORBIDDEN.getId()
            )
        );
    }
    
    @Test
    public void deactivateProtectedUserBadRequest() throws Exception {
      deactivate(UserConstants.ADMIN_USER_ID)
        .andExpect(
          status()
            .isBadRequest()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.USER_PROTECTED.getId()
            )
        );
    }
    
  }
  //endregion Activity
  
  //region Permissions
  @Nested
  class Permissions {
    
    @Test
    public void withoutTokenUnauthorized() throws Exception {
      UserCreateModel createModel = UserModelFactory.createModel(
        UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD
      );
      
      mockMvc.perform(
        post(UserController.URL)
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(createModel)
          )
      )
        .andExpect(
          status()
            .isUnauthorized()
        );
    }
    
    @Test
    public void teacherForbiddenOnAllManagementEndpoints() throws Exception {
      UUID teacherId = createUser(UserModelFactory.IVAN_USERNAME, UserModelFactory.IVAN_PASSWORD);
      UUID otherId = createUser(UserModelFactory.ALEXANDRA_USERNAME, UserModelFactory.ALEXANDRA_PASSWORD);
      
      assignRole(UserModelFactory.IVAN_USERNAME, RoleType.TEACHER);
      
      String teacherToken = bearerTokenFor(UserModelFactory.IVAN_USERNAME);
      
      UserCreateModel createModel = UserModelFactory.createModel("new.user", UserModelFactory.IVAN_PASSWORD);
      UserUpdateModel updateModel = UserModelFactory.updateModel(otherId, "changed");
      
      mockMvc.perform(
        post(UserController.URL)
          .header(
            HttpHeaders.AUTHORIZATION,
            teacherToken
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(createModel)
          )
      )
        .andExpect(
          status()
            .isForbidden()
        )
        .andExpect(
          jsonPath("$.errorCode")
            .value(
              ErrorCode.ACCESS_DENIED.getId()
            )
        );
      
      mockMvc.perform(
        get(UserController.URL + "/" + otherId)
          .header(
            HttpHeaders.AUTHORIZATION,
            teacherToken
          )
      )
        .andExpect(
          status()
            .isForbidden()
        );
      
      mockMvc.perform(
        put(UserController.URL + "/" + otherId)
          .header(
            HttpHeaders.AUTHORIZATION,
            teacherToken
          )
          .contentType(MediaType.APPLICATION_JSON)
          .content(
            objectMapper.writeValueAsString(updateModel)
          )
      )
        .andExpect(
          status()
            .isForbidden()
        );
      
      mockMvc.perform(
        delete(UserController.URL + "/" + otherId)
          .header(
            HttpHeaders.AUTHORIZATION,
            teacherToken
          )
      )
        .andExpect(
          status()
            .isForbidden()
        );
      
      mockMvc.perform(
        post(UserController.URL + "/" + otherId + "/deactivate")
          .header(
            HttpHeaders.AUTHORIZATION,
            teacherToken
          )
      )
        .andExpect(
          status()
            .isForbidden()
        );
      
      mockMvc.perform(
        post(UserController.URL + "/" + otherId + "/activate")
          .header(
            HttpHeaders.AUTHORIZATION,
            teacherToken
          )
      )
        .andExpect(
          status()
            .isForbidden()
        );
      
      mockMvc.perform(
        get(UserController.URL + "/me")
          .header(
            HttpHeaders.AUTHORIZATION,
            teacherToken
          )
      )
        .andExpect(
          status()
            .isOk()
        )
        .andExpect(
          jsonPath("$.id")
            .value(
              teacherId.toString()
            )
        );
    }
    
  }
  //endregion Permissions
  
  /**
   * Creates the user through the API on behalf of admin and returns his id
   */
  private UUID createUser(String username, String password) throws Exception {
    UserCreateModel createModel = UserModelFactory.createModel(username, password);
    
    String responseJson = mockMvc.perform(
      post(UserController.URL)
        .header(
          HttpHeaders.AUTHORIZATION,
          adminBearerToken()
        )
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsString(createModel)
        )
    )
      .andExpect(
        status()
          .isCreated()
      )
      .andReturn()
      .getResponse()
      .getContentAsString();
    
    return UUID.fromString(
      objectMapper.readTree(responseJson)
        .get("id")
        .asText()
    );
  }
  
  private ResultActions deactivate(UUID id) throws Exception {
    String url = UserController.URL + "/" + id + "/deactivate";
    
    return mockMvc.perform(
      post(url)
        .header(
          HttpHeaders.AUTHORIZATION,
          adminBearerToken()
        )
    );
  }
  
  private ResultActions login(@NotBlank String username, @NotBlank String password) throws Exception {
    LoginRequest request = new LoginRequest();
    
    request.setUsername(username);
    request.setPassword(password);
    
    return mockMvc.perform(
      post(AuthController.URL + "/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
          objectMapper.writeValueAsString(request)
        )
    );
  }
  
  @NotBlank
  private String loginAndGetToken(@NotBlank String username, @NotBlank String password) throws Exception {
    String responseJson = login(username, password)
      .andExpect(
        status()
          .isOk()
      )
      .andReturn()
      .getResponse()
      .getContentAsString();
    
    return objectMapper.readTree(responseJson)
      .get("token")
      .asText();
  }
  
}