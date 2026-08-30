package com.gmail.llemaxiss.app.user.entity;

import com.gmail.llemaxiss.app.common.entity.AbstractEntity;
import com.gmail.llemaxiss.app.userRole.entity.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.gmail.llemaxiss.app.common.property.component.AppProperty.TABLE_PREFIX;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = TABLE_PREFIX + "user")
public class User extends AbstractEntity {

  @NotNull
  @Column(name = "username", nullable = false)
  private String username;

  @NotNull
  @Column(name = "password", nullable = false)
  private String password;

  @NotNull
  @Builder.Default
  @Column(name = "is_active", nullable = false)
  private Boolean isActive = false;
  
  @Builder.Default
  @ToString.Exclude
  @OneToMany(
    mappedBy = "user",
    fetch = FetchType.LAZY,
    cascade = {CascadeType.ALL}
  )
  private Set<UserRole> userRoles = new HashSet<>();
}
