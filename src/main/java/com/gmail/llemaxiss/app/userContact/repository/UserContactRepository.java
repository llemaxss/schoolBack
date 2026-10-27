package com.gmail.llemaxiss.app.userContact.repository;

import com.gmail.llemaxiss.app._common.repository.CommonRepository;
import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userContact.enums.ContactType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserContactRepository extends CommonRepository<UserContact> {
  
  @NotNull
  List<UserContact> findByUserProfileId(@NotNull UUID userProfileId, @NotNull Sort sort);
  
  boolean existsByUserProfileIdAndTypeAndValue(@NotNull UUID userProfileId,
                                               @NotNull ContactType type,
                                               @NotNull String value);
  
  @NotNull
  Optional<UserContact> findByUserProfileIdAndTypeAndIsPrimaryTrue(@NotNull UUID userProfileId,
                                                                   @NotNull ContactType type);
  
}