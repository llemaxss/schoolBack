[Back](../README.md)

# Project structure

All packages live under `com.gmail.llemaxiss.app`.

`_common` - shared infrastructure (the core of the project)

Framework logic that everything else depends on.

| Package/class                     | Description                                                                                                                                                                                    |
|-----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `entity/CommonEntityId`           | Base entity: `id` (UUID, generated), equals/hashCode by id                                                                                                                                     |
| `entity/CommonEntity`             | Extends CommonEntityId: audit fields (`createTs/createdBy/updateTs/updatedBy/deleteTs/deletedBy`), `@Version` (optimistic locking), attaches the soft-delete filter                            |
| `repository/CommonRepository`     | Base repository (`JpaRepository<E, UUID>`), `@NoRepositoryBean`                                                                                                                                |
| `repository/CommonRepositoryImpl` | **Important:** standard `delete()` is overridden to soft-delete (sets `deleteTs`/`deletedBy`). Batch-delete methods throw `UnsupportedOperationException` - hard delete is forbidden by design |
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
