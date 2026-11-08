package com.gmail.llemaxiss.app.user.entity;

import com.gmail.llemaxiss.app._common.entity.CommonEntity;
import com.gmail.llemaxiss.app._common.hibernateFilter.util.CommonHibernateFilterConstants;
import com.gmail.llemaxiss.app.user.hibernateFilter.util.UserHibernateFilterConstants;
import com.gmail.llemaxiss.app.userRole.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import static com.gmail.llemaxiss.app._common.component.AppProperty.TABLE_PREFIX;

@NoArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = TABLE_PREFIX + "user")
@Filters({
  @Filter(name = UserHibernateFilterConstants.USER_ACTIVE_FILTER_NAME)
})
public class User extends CommonEntity {
  
  @NotBlank
  @Column(name = "username", nullable = false)
  private String username;
  
  @ToString.Exclude
  @NotBlank
  @Column(name = "password", nullable = false)
  private String password;

  @NotNull
  @Column(name = "is_active", nullable = false)
  private Boolean isActive = false;
  
  @ToString.Exclude
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @Filters({
    @Filter(name = CommonHibernateFilterConstants.SOFT_DELETE_FILTER_NAME)
  })
  private Set<UserRole> userRoles = new HashSet<>();
}
