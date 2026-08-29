package com.gmail.llemaxiss.app.role.entity;

import com.gmail.llemaxiss.app.common.entity.AbstractEntity;
import com.gmail.llemaxiss.app.role.enums.RoleType;
import com.gmail.llemaxiss.app.userRole.entity.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.TABLE_PREFIX;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = TABLE_PREFIX + "role")
public class Role extends AbstractEntity {

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "type_", nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private RoleType type;

  @Builder.Default
  @ToString.Exclude
  @OneToMany(
    mappedBy = "role",
    fetch = FetchType.LAZY,
    cascade = {CascadeType.ALL}
  )
  private Set<UserRole> userRoles = new HashSet<>();
}
