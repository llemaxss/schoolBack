[Back](../README.md)

# Adding a new CRUD domain

1. **Liquibase**: create a changeset (see DB section) - table named `school_<name>` with audit columns (`version`, `create_ts`, `created_by`, `update_ts`, `updated_by`, `delete_ts`, `deleted_by`).
2. **Entity**: extends `CommonEntity`, `@Table(name = TABLE_PREFIX + "...")` (always take the prefix from the constant!), add `@Filters(...)` if hibernate filters apply.
3. **Repository**: interface `XRepository extends CommonRepository<X>`.
4. **Models**: request/response under `model/`, response model extends `CommonEntityModel`.
5. **Mapper**: static class, map common fields via `CommonMapper.mapEntityFields(...)`.
6. **Service**: interface + impl `extends CommonService`, `@Transactional` on methods, errors via `throw new CommonException(ErrorCode.X, "...")`.
7. **Controller**: extends `CommonController`, `@RequestMapping(API_URL_PART + "/xs")`, Swagger annotations (`@Tag`, `@Operation`, `@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)`).
8. **ErrorCode**: add your codes to the enum (frontend localizes by string id).
9. **Tests**: service unit tests under `src/test/.../unit/<domain>/service`.
