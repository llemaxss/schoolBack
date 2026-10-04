package com.gmail.llemaxiss.app.unit.role.service;

import com.gmail.llemaxiss.app._common.enums.ErrorCode;
import com.gmail.llemaxiss.app._common.exception.model.response.CommonException;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.role.model.request.RoleCreateModel;
import com.gmail.llemaxiss.app.role.model.request.RoleUpdateModel;
import com.gmail.llemaxiss.app.role.repository.RoleRepository;
import com.gmail.llemaxiss.app.role.service.RoleServiceImpl;
import com.gmail.llemaxiss.app.unit._common.CommonServiceImplTest;
import com.gmail.llemaxiss.app.unit.role.modelFactory.RoleModelFactory;
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
public class RoleServiceImplTest extends CommonServiceImplTest {
  
  @Mock
  private RoleRepository roleRepository;
  
  @Spy
  @InjectMocks
  private RoleServiceImpl roleService;
  
  @BeforeEach
  public void setUp() throws Exception {
    setUpEntityManager(roleService);
  }
  
  //region Create
  @Nested
  class Create {
    
    @Test
    public void createSuccessfully() {
      Role savedRole = RoleModelFactory.adminEntity();
      RoleCreateModel model = RoleModelFactory.adminCreateModel();
      
      when(
        roleRepository.existsByName(
          model.getName()
        )
      )
        .thenReturn(false);
      
      when(
        roleRepository.save(
          any(Role.class)
        )
      )
        .thenReturn(savedRole);
      
      Role result = roleService.createRole(model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(
          savedRole.getId()
        );
      
      assertThat(
        result.getName()
      )
        .isEqualTo(
          model.getName()
        );
      
      assertThat(
        result.getType()
      )
        .isEqualTo(
          model.getType()
        );
      
      verify(roleRepository)
        .existsByName(
          model.getName()
        );
      
      verify(roleRepository)
        .save(
          any(Role.class)
        );
    }
    
    @Test
    void nameAlreadyExistsCommonException() {
      RoleCreateModel model = RoleModelFactory.adminCreateModel();
      
      when(
        roleRepository.existsByName(
          model.getName()
        )
      )
        .thenReturn(true);
      
      assertThatThrownBy(() -> {
        roleService.createRole(model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
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
      
      Role oldRole = RoleModelFactory.adminEntity();
      oldRole.setId(id);
      
      Role newRole = RoleModelFactory.teacherEntity();
      newRole.setId(id);
      
      RoleUpdateModel model = RoleModelFactory.teacherUpdateModel();
      model.setId(id);

      when(
        roleRepository.existsByName(
          model.getName()
        )
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
        .getRoleById(id);

      Role result = roleService.updateRole(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(
          newRole.getId()
        );
      
      assertThat(
        result.getName()
      )
        .isEqualTo(
          model.getName()
        );
      
      assertThat(
        result.getType()
      )
        .isEqualTo(
          model.getType()
        );
      
      verify(roleRepository)
        .existsByName(
          model.getName()
        );
      
      verify(roleRepository)
        .save(
          any(Role.class)
        );
    }

    @Test
    public void updateExcludeNameSuccessfully() {
      UUID id = UUID.randomUUID();
      
      Role oldRole = RoleModelFactory.adminEntity();
      oldRole.setId(id);
      
      Role newRole = RoleModelFactory.teacherEntity();
      newRole.setId(id);
      newRole.setName(oldRole.getName());
      
      RoleUpdateModel model = RoleModelFactory.teacherUpdateModel();
      model.setId(id);
      model.setName(oldRole.getName());
      
      when(
        roleRepository.save(
          any(Role.class)
        )
      )
        .thenReturn(newRole);

      doReturn(oldRole)
        .when(roleService)
        .getRoleById(id);

      Role result = roleService.updateRole(id, model);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
        .isEqualTo(id);
      
      assertThat(
        result.getName()
      )
        .isEqualTo(
          oldRole.getName()
        );
      
      assertThat(
        result.getType()
      )
        .isEqualTo(
          model.getType()
        );
      
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
      
      Role role = RoleModelFactory.adminEntity();
      role.setId(id);
      role.setName(RoleModelFactory.TEACHER_NAME);
      
      RoleUpdateModel model = RoleModelFactory.adminUpdateModel();
      model.setId(id);
      
      when(
        roleRepository.existsByName(
          model.getName()
        )
      )
        .thenReturn(true);
      
      doReturn(role)
        .when(roleService)
        .getRoleById(id);
      
      assertThatThrownBy(() -> {
        roleService.updateRole(id, model);
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
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
      
      Role role = RoleModelFactory.adminEntity();
      role.setId(id);
      
      doReturn(role)
        .when(roleService)
        .getRoleById(id);
      
      roleService.deleteRole(id);
      
      verify(
        roleService
      )
        .getRoleById(id);
      
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
      
      Role role = RoleModelFactory.adminEntity();
      role.setId(id);
      
      when(
        roleRepository.findById(id)
      )
        .thenReturn(
          Optional.of(role)
        );
      
      Role result = roleService.getRoleById(id);
      
      assertThat(result)
        .isNotNull();
      
      assertThat(
        result.getId()
      )
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
        roleService.getRoleById(
          UUID.randomUUID()
        );
      })
        .isInstanceOf(CommonException.class)
        .hasFieldOrPropertyWithValue(
          ERROR_CODE_FIELD,
          ErrorCode.ROLE_NOT_FOUND
        );
    }
    
  }
  //endregion Other

}
