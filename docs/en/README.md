[Back](../../README.md)

# _School management system (Backend)_

Spring Boot REST API for a school management system: users, roles, permissions, profiles.

## Tech stack

| Tool              | Description                                                         |
|-------------------|---------------------------------------------------------------------|
| Java 21           | language (Gradle toolchain enforces the version)                    |
| Spring Boot 3.5.x | web, data-jpa, security, validation                                 |
| PostgreSQL        | database                                                            |
| Liquibase         | schema migrations (run on application startup)                      |
| JWT (jjwt)        | stateless authentication                                            |
| springdoc-openapi | Swagger UI: `http://localhost:8080/swagger-ui.html`                 |
| Lombok            | boilerplate generation (`@Getter/@Setter/@RequiredArgsConstructor`) |
| Log4j2            | logging (logback is excluded in build.gradle)                       |

## Project structure

All packages live under `com.gmail.llemaxiss.app`.

### `_common` — shared infrastructure (the core of the project)

Framework logic that everything else depends on.

| Package/class                     | Description                                                                                                                                                                                    |
|-----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `entity/CommonEntityId`           | Base entity: `id` (UUID, generated), equals/hashCode by id                                                                                                                                     |
| `entity/CommonEntity`             | Extends CommonEntityId: audit fields (`createTs/createdBy/updateTs/updatedBy/deleteTs/deletedBy`), `@Version` (optimistic locking), attaches the soft-delete filter                            |
| `repository/CommonRepository`     | Base repository (`JpaRepository<E, UUID>`), `@NoRepositoryBean`                                                                                                                                |
| `repository/CommonRepositoryImpl` | **Important:** standard `delete()` is overridden to soft-delete (sets `deleteTs`/`deletedBy`). Batch-delete methods throw `UnsupportedOperationException` — hard delete is forbidden by design |
| `service/CommonService`           | Abstract service base: provides protected `EntityManager em`                                                                                                                                   |
| `mapper/CommonMapper`             | Static helpers mapping common entity fields into models                                                                                                                                        |
| `component/AppProperty`           | Configuration + global constants: `API_URL_PART = "/api/v1"`, `TABLE_PREFIX = "school_"`, `SPRING_ROLE_PREFIX = "ROLE_"`, jwt secret/expiration from `app.*` properties                        |
| `enums/ErrorCode` + `exception/*` | Unified error handling (see below)                                                                                                                                                             |
| `enums/common/*`                  | `CommonEnum<T>`/`CommonStringEnum` interfaces + Jackson deserializer for enums by id                                                                                                           |
| `hibernateFilter/*`               | Hibernate filter mechanism (see dedicated section)                                                                                                                                             |
| `security/*`                      | JWT authentication and Spring Security (see dedicated section)                                                                                                                                 |
| `config/OpenApiConfig`            | Swagger security scheme (`bearerAuth`)                                                                                                                                                         |

Standard domain layout:
- `controller/`,
- `entity/`,
- `enums/`,
- `mapper/`,
- `model/request/`,
- `model/response/`,
- `repository/`,
- `service/` (interface + `*ServiceImpl`).

## Adding a new CRUD domain

1. **Liquibase**: create a changeset (see DB section) — table named `school_<name>` with audit columns (`version`, `create_ts`, `created_by`, `update_ts`, `updated_by`, `delete_ts`, `deleted_by`).
2. **Entity**: extends `CommonEntity`, `@Table(name = TABLE_PREFIX + "...")` (always take the prefix from the constant!), add `@Filters(...)` if hibernate filters apply.
3. **Repository**: interface `XRepository extends CommonRepository<X>`.
4. **Models**: request/response under `model/`, response model extends `CommonEntityModel`.
5. **Mapper**: static class, map common fields via `CommonMapper.mapEntityFields(...)`.
6. **Service**: interface + impl `extends CommonService`, `@Transactional` on methods, errors via `throw new CommonException(ErrorCode.X, "...")`.
7. **Controller**: extends `CommonController`, `@RequestMapping(API_URL_PART + "/xs")`, Swagger annotations (`@Tag`, `@Operation`, `@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)`).
8. **ErrorCode**: add your codes to the enum (frontend localizes by string id).
9. **Tests**: service unit tests under `src/test/.../unit/<domain>/service`.

## Hibernate filters (`_common/hibernateFilter`)

Custom query-filtering mechanism at the Hibernate level. Two main filters exist:
- **soft delete** (`SOFT_DELETE_FILTER_NAME`) — based on the `delete_ts` column;
- **active users** (`USER_ACTIVE_FILTER_NAME`) — based on the `is_active` column (`User` only).

How it works:
- `config/HibernateFilterConfig` registers `component/HibernateFilterInterceptor` as a Hibernate interceptor.
- The interceptor in `afterTransactionBegin` **automatically enables for every transaction**: "not deleted only" + "active users only". By default, deleted/deactivated records are simply invisible.
- `component/HibernateFilterManager` — programmatic control: `enableDeletedOnlyFilter()`, `disableSoftDeleteFilter()`, etc. (e.g. so an admin can see deleted users). It uses `ObjectProvider<EntityManager>` deliberately — this breaks the circular dependency Config→Interceptor→Manager→EntityManager.
- The `@FilterDef`s themselves (names, parameters, SQL conditions) are declared in `package-info.java`: common ones in `_common/hibernateFilter`, domain ones in `user/hibernateFilter`. Constants live in `util/HibernateFilterConstants`. A new filter = new `@FilterDef` in package-info + constant + Manager method + `@Filter` on the entity.

Note: filters only apply where `@Filter(...)` is declared on the entity/association — see `User`, `UserProfile`, `CommonEntity`.

## Security (`_common/security`)

- Stateless JWT: `JwtAuthenticationFilter` (validates the Bearer token), `JwtHelper` (generation/validation), secrets in `AppProperty` / `application.yml` (`app.jwt-secret`, `app.jwt-expiration-millis`).
- `SecurityConfig`: permitAll only for `/api/v1/auth/**`, `/api/v1/info/**`, `/actuator/health/**` and swagger paths; everything else requires authentication. CSRF disabled.
- Login: `AuthController` (`/api/v1/auth`) returns a token; user authorities are built in `UserUtil.getAuthorities(User)` from roles (`ROLE_<type>`) and permissions.
- Current user: `user/util/UserUtil` (`getUsername()` is used, among other things, by soft delete to fill `deletedBy`).

## Database & Liquibase

- `ddl-auto: validate` — Hibernate **never creates** anything; schema comes only from migrations.
- Master changelog: `src/main/resources/db/changelog/changelog.yml` → year/month hierarchy: `26/26_changelog.yml` → `26/08/2608_changelog.yml` → concrete files.
- Changeset file naming: `DDMMNN_description.yml` (e.g. `260829_1_createUserTable.yml`), NN = sequential number within the day. New months/years require an include in the parent changelog.
- All tables use the `school_` prefix. Default admin user and roles are seeded by a changeset (`insertDefaultUserAndRoles`).

## Configuration

`src/main/resources/application.yml` — three blocks separated by `---`: root, `DEV` profile (default, DEBUG logs, show-sql), `PROD` profile. Switch via `spring.profiles.active`. Port 8080.

## API errors

All exceptions are caught by `GlobalExceptionHandler` (@RestControllerAdvice) and returned as `ErrorResponse`.
Business errors — `CommonException` → HTTP 400.
`@Valid` validation failures are mapped into `fieldErrors` automatically.

## Tests

- Service unit tests: `src/test/java/.../app/unit/<domain>/service`, `Mockito` (`@ExtendWith(MockitoExtension.class)`).
- Base class: `unit/_common/CommonServiceImplTest` — wires a mock `EntityManager` for services extending `CommonService(setUpEntityManager(roleService))`; service tests extend it.
- Mock model factories: `<domain>/modelFactory/XModelFactory` (example: `RoleModelFactory`).
- Test structure pattern: @Nested classes per operation (`Create/Update/...`).

## Environment variables

| Variable                                                     | YAML key                                         | Default value | Description    |
|--------------------------------------------------------------|--------------------------------------------------|---------------|----------------|
| COMMON                                                       |                                                  |               |                |
| `SPRING_PROFILES_ACTIVE`                                     | `spring.profiles.active`                         | `DEV`         | Active profile |
| `SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA`             | `spring.jpa.properties.hibernate.default_schema` | `public`      | DB schema      |
| DEV PROFILE                                                  |                                                  |               |                |
| `DEV__SPRING_DATASOURCE_HOST`                                | `spring.datasource.url`                          |               | DB host        |
| `DEV__SPRING_DATASOURCE_PORT`                                | `spring.datasource.url`                          |               | DB port        |
| `DEV__SPRING_DATASOURCE_NAME`                                | `spring.datasource.url`                          |               | DB name        |
| `DEV__SPRING_DATASOURCE_USERNAME`                            | `spring.datasource.username`                     |               | DB username    |
| `DEV__SPRING_DATASOURCE_PASSWORD`                            | `spring.datasource.password`                     |               | DB password    |
| `DEV__SERVER_PORT`                                           | `server.port`                                    |               | Server port    |
| `DEV__APP_JWT_SECRET`                                        | `app.jwt-secret`                                 |               | JWT secret key |
| `DEV__APP_JWT_EXPIRATION_MILLIS`                             | `app.jwt-expiration-millis`                      |               | JWT TTL, ms    |
| PROD PROFILE                                                 |                                                  |               |                |
| `PROD__SPRING_DATASOURCE_HOST`                               | `spring.datasource.url`                          |               | DB host        |
| `PROD__SPRING_DATASOURCE_PORT`                               | `spring.datasource.url`                          |               | DB port        |
| `PROD__SPRING_DATASOURCE_NAME`                               | `spring.datasource.url`                          |               | DB name        |
| `PROD__SPRING_DATASOURCE_USERNAME`                           | `spring.datasource.username`                     |               | DB username    |
| `PROD__SPRING_DATASOURCE_PASSWORD`                           | `spring.datasource.password`                     |               | DB password    |
| `PROD__SERVER_PORT`                                          | `server.port`                                    |               | Server port    |
| `PROD__APP_JWT_SECRET`                                       | `app.jwt-secret`                                 |               | JWT secret key |
| `PROD__APP_JWT_EXPIRATION_MILLIS`                            | `app.jwt-expiration-millis`                      |               | JWT TTL, ms    |

## Build & run

```bash
./gradlew bootRun # run (requires PostgreSQL on localhost:5432, database school_dev)
./gradlew test    # unit tests
./gradlew build   # build jar