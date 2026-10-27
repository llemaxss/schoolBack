[Back](../README.md)

# Security

(`_common/security`)

- Stateless JWT: `JwtAuthenticationFilter` (validates the Bearer token), `JwtHelper` (generation/validation), secrets in `AppProperty` / `application.yml` (`app.jwt-secret`, `app.jwt-expiration-millis`).
- `SecurityConfig`: permitAll only for `/api/v1/auth/**`, `/api/v1/info/**`, `/actuator/health/**` and swagger paths; everything else requires authentication. CSRF disabled.
- Login: `AuthController` (`/api/v1/auth`) returns a token; user authorities are built in `UserUtil.getAuthorities(User)` from roles (`ROLE_<type>`) and permissions.
- Current user: `user/util/UserUtil` (`getUsername()`).
