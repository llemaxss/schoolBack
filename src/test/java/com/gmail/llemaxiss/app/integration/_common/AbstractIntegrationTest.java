package com.gmail.llemaxiss.app.integration._common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.llemaxiss.app._common.entity.CommonEntity;
import com.gmail.llemaxiss.app.role.entity.Role;
import com.gmail.llemaxiss.app.user.entity.User;
import com.gmail.llemaxiss.app.userContact.entity.UserContact;
import com.gmail.llemaxiss.app.userProfile.entity.UserProfile;
import com.gmail.llemaxiss.app.userRole.entity.UserRole;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Base class for all integration tests
 *
 * <p>
 * Starts one PostgreSQL container for the whole test run,
 * applies Liquibase migrations on it and cleans the data after each test.
 * Data inserted by migrations is kept.
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
  
  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");
  
  /**
   * Tables to clean after each test: children first, because of foreign keys
   */
  private static final Set<String> TABLES_TO_CLEAN = getTableNames(
    UserContact.class, UserProfile.class,
    UserRole.class, Role.class, User.class
  );
  
  /**
   * Tables which already have rows right after migrations
   */
  private static final Set<String> TABLES_WITH_SEED_DATA = getTableNames(
    User.class, Role.class, UserRole.class
  );
  
  /**
   * Ids of the rows inserted by migrations, by table (filled once before the first test)
   */
  private static final Map<String, List<UUID>> SEED_IDS = new HashMap<>();
  
  static {
    POSTGRES.start();
  }
  
  @DynamicPropertySource
  static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
  }
  
  @Autowired
  protected MockMvc mockMvc;
  
  @Autowired
  protected ObjectMapper objectMapper;
  
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;
  
  @BeforeEach
  protected void saveSeedData() {
    if (!CollectionUtils.isEmpty(SEED_IDS)) {
      return;
    }
    
    for (String table : TABLES_WITH_SEED_DATA) {
      List<UUID> ids = jdbcTemplate.getJdbcTemplate()
        .queryForList("SELECT id FROM " + table, UUID.class);
      
      SEED_IDS.put(table, ids);
    }
  }
  
  @AfterEach
  protected void cleanUpDatabase() {
    for (String table : TABLES_TO_CLEAN) {
      if (SEED_IDS.containsKey(table)) {
        jdbcTemplate.update(
          "DELETE FROM " + table + " WHERE id NOT IN (:seedIds)",
          Map.of("seedIds", SEED_IDS.get(table))
        );
      } else {
        jdbcTemplate.getJdbcTemplate()
          .update("DELETE FROM " + table);
      }
    }
  }
  
  @SafeVarargs
  @NotNull
  private static Set<String> getTableNames(@NotNull Class<? extends CommonEntity>... classes) {
    Set<String> tableNames = new HashSet<>();
    
    for (Class<? extends CommonEntity> clazz : classes) {
      String tableName = clazz.getAnnotation(Table.class).name();
      
      tableNames.add(tableName);
    }
    
    return tableNames;
  }
}