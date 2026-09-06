package com.gmail.llemaxiss.app;

import com.gmail.llemaxiss.app.common.repository.CommonRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
  repositoryBaseClass = CommonRepositoryImpl.class
)
public class SchoolBackApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(SchoolBackApplication.class, args);
  }
  
}
