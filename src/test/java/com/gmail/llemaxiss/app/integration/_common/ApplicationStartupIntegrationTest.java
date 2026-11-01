package com.gmail.llemaxiss.app.integration._common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationStartupIntegrationTest extends AbstractIntegrationTest {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  @Test
  public void contextLoadsAndMigrationsApplied() {
    Integer adminCount = jdbcTemplate.queryForObject(
      "SELECT count(*) FROM school_user WHERE username = 'admin'",
      Integer.class
    );
    
    Integer roleCount = jdbcTemplate.queryForObject(
      "SELECT count(*) FROM school_role",
      Integer.class
    );
    
    assertThat(adminCount)
      .isEqualTo(1);
    
    assertThat(roleCount)
      .isEqualTo(7);
  }
  
  @Test
  public void userContactTableExistsAndIsEmpty() {
    Integer contactCount = jdbcTemplate.queryForObject(
      "SELECT count(*) FROM school_user_contact",
      Integer.class
    );
    
    assertThat(contactCount)
      .isZero();
  }
  
}