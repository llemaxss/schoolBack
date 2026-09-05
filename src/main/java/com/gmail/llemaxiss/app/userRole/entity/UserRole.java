package com.gmail.llemaxiss.app.userRole.entity;

import com.gmail.llemaxiss.app.common.entity.AbstractEntity;
import com.gmail.llemaxiss.app.common.hibernateFilter.FilterConstants;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.TABLE_PREFIX;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = TABLE_PREFIX + "user_role")
@Filter(name = FilterConstants.SOFT_DELETE_FILTER_NAME)
public class UserRole extends AbstractEntity {

  @NotNull
  @ManyToOne(optional = false)
  @JoinColumn(
    name = "user_id",
    referencedColumnName = "id",
    nullable = false
  )
  @Filter(name = FilterConstants.SOFT_DELETE_FILTER_NAME)
  private User user;

  @NotNull
  @ManyToOne(optional = false)
  @JoinColumn(
    name = "role_id",
    referencedColumnName = "id",
    nullable = false
  )
  @Filter(name = FilterConstants.SOFT_DELETE_FILTER_NAME)
  private Role role;
  
}
