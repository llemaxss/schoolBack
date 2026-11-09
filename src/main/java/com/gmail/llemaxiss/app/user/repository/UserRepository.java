package com.gmail.llemaxiss.app.user.repository;

import com.gmail.llemaxiss.app._common.repository.CommonRepository;
import com.gmail.llemaxiss.app.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CommonRepository<User> {
  
  Optional<User> findByUsername(String username);
  
  boolean existsByUsername(@NotBlank String username);
  
}
