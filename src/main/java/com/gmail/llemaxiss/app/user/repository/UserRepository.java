package com.gmail.llemaxiss.app.user.repository;

import com.gmail.llemaxiss.app._common.repository.CommonRepository;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User> {

  @Nullable
  User findByUsername(String username);
  
  boolean existsByUsername(@NotNull String username);
  
}
