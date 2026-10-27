[Назад](../README.md)

# Безопасность

(`_common/security`)

- Stateless JWT: `JwtAuthenticationFilter` (проверяет Bearer-токен)
- `JwtHelper` (генерация/валидация), секретный ключ в `AppProperty` / `application.yml` (`app.jwt-secret`, `app.jwt-expiration-millis`).
- `SecurityConfig`: permitAll только на `/api/v1/auth/**`, `/api/v1/info/**`, `/actuator/health/**`, swagger-пути; всё остальное - аутентификация. CSRF выключен.
- Логин: `AuthController` (`/api/v1/auth`) возвращает токен; authorities юзера собираются в `UserUtil.getAuthorities(User)` из ролей (`ROLE_<type>`) и permissions.
- Текущий юзер: `user/util/UserUtil` (`getUsername()`).
