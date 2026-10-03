package com.gmail.llemaxiss.app.user.repository;

import com.gmail.llemaxiss.app._common.repository.CommonRepository;
import com.gmail.llemaxiss.app.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CommonRepository<User> {

  User findByUsername(String username);
  
}
