package com.gmail.llemaxiss.app.userContact.entity;

import com.gmail.llemaxiss.app._common.entity.CommonEntity;
import com.gmail.llemaxiss.app._common.hibernateFilter.util.HibernateFilterConstants;
import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = TABLE_PREFIX + "user_contact")
public class UserContact extends CommonEntity {
  
  @ToString.Exclude
  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(
    name = "user_profile_id",
    referencedColumnName = "id",
    nullable = false
  )
  @Filters({
    @Filter(name = HibernateFilterConstants.SOFT_DELETE_FILTER_NAME)
  })
  private UserProfile userProfile;
  
  @NotNull
  @Column(name = "type_", nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private ContactType type;
  
  @NotNull
  @Column(name = "value_", nullable = false)
  private String value;
  
  @NotNull
  @Column(name = "is_primary", nullable = false)
  private Boolean isPrimary = false;
  
  @Column(name = "comment", length = 500)
  private String comment;
  
}