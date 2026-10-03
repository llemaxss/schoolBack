package com.gmail.llemaxiss.app.userProfile.entity;

import com.gmail.llemaxiss.app._common.entity.CommonEntity;
import com.gmail.llemaxiss.app._common.hibernateFilter.util.HibernateFilterConstants;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.userProfile.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import java.time.LocalDate;

import static com.gmail.llemaxiss.app._common.component.AppProperty.TABLE_PREFIX;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Getter
@Setter
@Entity
@Table(name = TABLE_PREFIX + "user_profile")
public class UserProfile extends CommonEntity {
  
  @NotNull
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(
    name = "user_id",
    referencedColumnName = "id",
    nullable = false
  )
  @Filters({
    @Filter(name = HibernateFilterConstants.SOFT_DELETE_FILTER_NAME),
    @Filter(name = HibernateFilterConstants.USER_ACTIVE_FILTER_NAME),
  })
  private User user;
  
  @NotNull
  @Column(name = "first_name", nullable = false)
  private String firstName;
  
  @NotNull
  @Column(name = "last_name", nullable = false)
  private String lastName;
  
  @Column(name = "middle_name")
  private String middleName;
  
  @NotNull
  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;
  
  @NotNull
  @Column(name = "gender", nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private Gender gender;
  
}
