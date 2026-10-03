package com.gmail.llemaxiss.app.userProfile.repository;

import com.gmail.llemaxiss.app._common.repository.CommonRepository;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends CommonRepository<UserProfile> {
  
  boolean existsByUserId(@NotNull UUID userId);
  
}
