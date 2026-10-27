[Назад](../README.md)

# Создание нового CRUD-домена

1. **Liquibase**: создать changeset - таблица обязательно с именем `school_<name>` и аудит-колонками (`version`, `create_ts`, `created_by`, `update_ts`, `updated_by`, `delete_ts`, `deleted_by`).
2. **Entity**: extends `CommonEntity`, `@Table(name = TABLE_PREFIX + "...")` (префикс брать только из константы!), при необходимости `@Filters(...)` для хибейрнейт-фильтров.
3. **Repository**: interface `XRepository extends CommonRepository<X>`.
4. **Models**: request/response в `model/`, response-модель extends `CommonEntityModel`.
5. **Mapper**: статический класс, общие поля маппить через `CommonMapper.mapEntityFields(...)`.
6. **Service**: interface + impl `extends CommonService`, `@Transactional` на методах, ошибки - `throw new CommonException(ErrorCode.X, "...")`.
7. **Controller**: extends `CommonController`, `@RequestMapping(API_URL_PART + "/xs")`, аннотации Swagger (`@Tag`, `@Operation`, `@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)`).
8. **ErrorCode**: добавить свои коды в enum (фронт локализует по строковому id).
9. **Тесты**: unit-тест сервиса в `src/test/.../unit/<domain>/service`.
