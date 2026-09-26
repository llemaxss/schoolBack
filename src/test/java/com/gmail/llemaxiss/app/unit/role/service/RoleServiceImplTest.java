package com.gmail.llemaxiss.app.unit.role.service;

import com.gmail.llemaxiss.app.common.enums.ErrorCode;
import com.gmail.llemaxiss.app.common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.role.model.request.RoleCreateModel;
import com.gmail.llemaxiss.app.role.model.request.RoleUpdateModel;
import com.gmail.llemaxiss.app.role.repository.RoleRepository;
import com.gmail.llemaxiss.app.role.service.RoleServiceImpl;
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
public class RoleServiceImplTest {
  
  @Mock
  private RoleRepository roleRepository;
  
  @Spy
  @InjectMocks
  private RoleServiceImpl roleService;
  
  //region Create
  @Nested
  class Create {
    
    @Test
    public void createSuccessfully() {
      String name = "ADMIN";
      RoleType type = RoleType.ADMIN;
      
      RoleCreateModel model = new RoleCreateModel(name, type);
      
      Role savedRole = new Role();
      
      savedRole.setId(UUID.randomUUID());
      savedRole.setName(name);
      savedRole.setType(type);
      
      when(
        roleRepository.existsByName(name)
      )
        .thenReturn(false);
      
      when(
        roleRepository.save(
          any(Role.class)
        )
      )
        .thenReturn(savedRole);
      
      Role result = roleService.create(model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(result.getId())
        .isEqualTo(savedRole.getId());
      
      assertThat(result.getName())
        .isEqualTo(name);
      
      assertThat(result.getType())
        .isEqualTo(type);
      
      verify(roleRepository)
        .existsByName(name);
      
      verify(roleRepository)
        .save(
          any(Role.class)
        );
    }
    
    @Test
    void nameAlreadyExistsCommonException() {
      String name = "ADMIN";
      RoleType type = RoleType.ADMIN;
      
      RoleCreateModel model = new RoleCreateModel(name, type);
      
      when(
        roleRepository.existsByName(name)
      )
        .thenReturn(true);
      
      assertThatThrownBy(() -> {
        roleService.create(model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          "errorCode",
          ErrorCode.ROLE_NAME_ALREADY_EXISTS
        );
      
      verify(
        roleRepository,
        never()
      )
        .save(
          any(Role.class)
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
      
      String oldName = "ADMIN";
      RoleType oldType = RoleType.ADMIN;
      
      String newName = "TEACHER";
      RoleType newType = RoleType.TEACHER;
      
      RoleUpdateModel model = new RoleUpdateModel(id, newName, newType);
      
      Role oldRole = new Role();
      
      oldRole.setId(id);
      oldRole.setName(oldName);
      oldRole.setType(oldType);
      
      Role newRole = new Role();
      
      newRole.setId(id);
      newRole.setName(newName);
      newRole.setType(newType);

      when(
        roleRepository.existsByName(newName)
      )
        .thenReturn(false);
      
      when(
        roleRepository.save(
          any(Role.class)
        )
      )
        .thenReturn(newRole);

      doReturn(oldRole)
        .when(roleService)
        .getById(id);

      Role result = roleService.update(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(result.getId())
        .isEqualTo(newRole.getId());
      
      assertThat(result.getName())
        .isEqualTo(newName);
      
      assertThat(result.getType())
        .isEqualTo(newType);
      
      verify(roleRepository)
        .existsByName(newName);
      
      verify(roleRepository)
        .save(
          any(Role.class)
        );
    }

    @Test
    public void updateExcludeNameSuccessfully() {
      UUID id = UUID.randomUUID();
      
      String oldName = "ADMIN";
      RoleType oldType = RoleType.ADMIN;
      
      RoleType newType = RoleType.TEACHER;
      
      RoleUpdateModel model = new RoleUpdateModel(id, oldName, newType);
      
      Role oldRole = new Role();
      
      oldRole.setId(id);
      oldRole.setName(oldName);
      oldRole.setType(oldType);
      
      Role newRole = new Role();
      
      newRole.setId(id);
      newRole.setName(oldName);
      newRole.setType(newType);
      
      when(
        roleRepository.save(
          any(Role.class)
        )
      )
        .thenReturn(newRole);

      doReturn(oldRole)
        .when(roleService)
        .getById(id);

      Role result = roleService.update(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(result.getId())
        .isEqualTo(newRole.getId());
      
      assertThat(result.getName())
        .isEqualTo(oldName);
      
      assertThat(result.getType())
        .isEqualTo(newType);
      
      verify(
        roleRepository,
        never()
      )
        .existsByName(
          any(String.class)
        );
      
      verify(roleRepository)
        .save(
          any(Role.class)
        );
    }

    @Test
    void nameAlreadyExistsCommonException() {
      UUID id = UUID.randomUUID();
      
      String newName = "ADMIN";
      RoleType type = RoleType.ADMIN;
      
      String oldName = "TEACHER";
      
      RoleUpdateModel model = new RoleUpdateModel(id, newName, type);
      
      Role role = new Role();
      
      role.setId(id);
      role.setName(oldName);
      role.setType(type);
      
      when(
        roleRepository.existsByName(newName)
      )
        .thenReturn(true);
      
      doReturn(role)
        .when(roleService)
        .getById(id);
      
      assertThatThrownBy(() -> {
        roleService.update(id, model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          "errorCode",
          ErrorCode.ROLE_NAME_ALREADY_EXISTS
        );
      
      verify(
        roleRepository,
        never()
      )
        .save(
          any(Role.class)
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
      
      Role role = new Role();
      
      role.setId(id);
      
      doReturn(role)
        .when(roleService)
        .getById(id);
      
      roleService.delete(id);
      
      verify(
        roleService
      )
        .getById(id);
      
      verify(
        roleRepository
      )
        .delete(role);
    }
    
  }
  //endregion Delete
  
  //region Other
  @Nested
  class Other {
    
    @Test
    public void getByIdSuccessfully() {
      UUID id = UUID.randomUUID();
      
      Role role = new Role();
      
      role.setId(id);
      
      when(
        roleRepository.findById(id)
      )
        .thenReturn(
          Optional.of(role)
        );
      
      Role result = roleService.getById(id);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(result.getId())
        .isEqualTo(id);
    }
    
    @Test
    public void roleNotFoundCommonException() {
      when(
        roleRepository.findById(
          any(UUID.class)
        )
      )
        .thenReturn(Optional.empty());
      
      assertThatThrownBy(() -> {
        roleService.getById(UUID.randomUUID());
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          "errorCode",
          ErrorCode.ROLE_NOT_FOUND
        );
    }
    
  }
  //endregion Other

}
