package com.gmail.llemaxiss.app.user.repository;

import java.util.UUID;

import com.gmail.llemaxiss.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  User findByUsername(String username);
  
}
