package com.gmail.llemaxiss.app.common.controller.appInfo;

import com.gmail.llemaxiss.app.common.property.component.AppProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gmail.llemaxiss.app.common.security.config.SecurityConfig.API_INFO_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_INFO_URL)
@Tag(
  name = "App info",
  description = "Application information"
)
public class AppInfoController {
  
  private final AppProperty appProperty;

  @GetMapping("/version")
  @Operation(
    summary = "Get application version",
    description = "Returns the current version of the application"
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Application version retrieved successfully",
      content = @Content(
        mediaType = MediaType.TEXT_PLAIN_VALUE,
        schema = @Schema(example = "1.0.0")
      )
    )
  })
  public ResponseEntity<String> getAppVersion() {
    String version = appProperty.getVersion();
    
    return ResponseEntity.ok(version);
  }

}
