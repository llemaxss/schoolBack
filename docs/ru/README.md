[Назад](../../README.md)

# _School management system (Backend)_

Spring Boot REST API для системы управления школой: пользователи, роли, permissions, профили.

## Стек

| Технология        | Описание                                            |
|-------------------|-----------------------------------------------------|
| Java 21           | язык                                                |
| Spring Boot 3.5.x | web, data-jpa, security, validation                 |
| PostgreSQL        | база данных                                         |
| Liquibase         | миграции схемы                                      |
| JWT (jjwt)        | stateless аутентификация                            |
| springdoc-openapi | Swagger UI: `http://localhost:8080/swagger-ui.html` |
| Lombok            | генерация boilerplate                               |
| Log4j2            | логирование                                         |

## Структура проекта

Все пакеты лежат под `com.gmail.llemaxiss.app`.

### `_common` - общая инфраструктура (главная папка проекта)

Здесь лежит framework-логика, от которой зависит весь остальной код.

| Пакет/класс                       | Описание                                                                                                                                                                                           |
|-----------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `entity/CommonEntityId`           | Базовая сущность: `id` (UUID, автогенерация), equals/hashCode по id                                                                                                                                |
| `entity/CommonEntity`             | Расширяет CommonEntityId: аудит-поля (`createTs/createdBy/updateTs/updatedBy/deleteTs/deletedBy`), `@Version` (оптимистичная блокировка), подключает soft-delete фильтр                            |
| `repository/CommonRepository`     | Базовый репозиторий (`JpaRepository<E, UUID>`), `@NoRepositoryBean`                                                                                                                                |
| `repository/CommonRepositoryImpl` | **Важно:** стандартный `delete()` переопределён в soft-delete (ставит `deleteTs`/`deletedBy`). Batch-delete методы бросают `UnsupportedOperationException` - хард-удаление запрещено принципиально |
| `service/CommonService`           | Абстрактный базис сервисов: даёт защищённый `EntityManager em`                                                                                                                                     |
| `mapper/CommonMapper`             | Статические helpers для маппинга общих полей сущности в модели                                                                                                                                     |
| `component/AppProperty`           | Конфигурация + глобальные константы: `API_URL_PART = "/api/v1"`, `TABLE_PREFIX = "school_"`, `SPRING_ROLE_PREFIX = "ROLE_"`, jwt-secret/jwt-expiration из `app.*` свойств                          |
| `enums/ErrorCode` + `exception/*` | Единая обработка ошибок (см. ниже)                                                                                                                                                                 |
| `enums/common/*`                  | Интерфейсы `CommonEnum<T>`/`CommonStringEnum` + Jackson-десериализатор enum'ов по id                                                                                                               |
| `hibernateFilter/*`               | Механизм Hibernate-фильтров (см. отдельный раздел)                                                                                                                                                 |
| `security/*`                      | JWT-аутентификация и Spring Security (см. отдельный раздел)                                                                                                                                        |
| `config/OpenApiConfig`            | Swagger security scheme (`bearerAuth`)                                                                                                                                                             |

Стандартная структура домена:
- `controller/`,
- `entity/`,
- `enums/`,
- `mapper/`,
- `model/request/`,
- `model/response/`,
- `repository/`,
- `service/` (interface + `*ServiceImpl`).

## Создание нового CRUD-домена

1. **Liquibase**: создать changeset - таблица обязательно с именем `school_<name>` и аудит-колонками (`version`, `create_ts`, `created_by`, `update_ts`, `updated_by`, `delete_ts`, `deleted_by`).
2. **Entity**: extends `CommonEntity`, `@Table(name = TABLE_PREFIX + "...")` (префикс брать только из константы!), при необходимости `@Filters(...)` для хибейрнейт-фильтров.
3. **Repository**: interface `XRepository extends CommonRepository<X>`.
4. **Models**: request/response в `model/`, response-модель extends `CommonEntityModel`.
5. **Mapper**: статический класс, общие поля маппить через `CommonMapper.mapEntityFields(...)`.
6. **Service**: interface + impl `extends CommonService`, `@Transactional` на методах, ошибки - `throw new CommonException(ErrorCode.X, "...")`.
7. **Controller**: extends `CommonController`, `@RequestMapping(API_URL_PART + "/xs")`, аннотации Swagger (`@Tag`, `@Operation`, `@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)`).
8. **ErrorCode**: добавить свои коды в enum (фронт локализует по строковому id).
9. **Тесты**: unit-тест сервиса в `src/test/.../unit/<domain>/service`.

## Hibernate filters (`_common/hibernateFilter`)

Кастомный механизм фильтрации запросов на уровне Hibernate. Два основных фильтра:
- **soft-delete** (`SOFT_DELETE_FILTER_NAME`) - по колонке `delete_ts`;
- **active users** (`USER_ACTIVE_FILTER_NAME`) - по колонке `is_active` (только активные `User`).

Как это работает:
- `config/HibernateFilterConfig` регистрирует `component/HibernateFilterInterceptor` как Hibernate interceptor.
- Interceptor в `afterTransactionBegin` **автоматически включает на каждую транзакцию**: «только неудалённые» + «только активные пользователи». То есть по умолчанию удалённые/деактивированные записи не видны вообще.
- `component/HibernateFilterManager` - программное управление: `enableDeletedOnlyFilter()`, `disableSoftDeleteFilter()` и т.п. (например, чтобы админ увидел удалённых пользователей). Работает через `ObjectProvider<EntityManager>` - так разорвана циклическая зависимость Config->Interceptor->Manager->EntityManager.
- Сами `@FilterDef` (имена, параметры, SQL-условия) объявлены в `package-info.java`: общие - в `_common/hibernateFilter`. Константы - в `util/HibernateFilterConstants`. Новый фильтр = новый `@FilterDef` в package-info + константа + метод в Manager + `@Filter` на сущности.

Внимание: фильтры действуют только там, где на entity/ассоциации висит `@Filter(...)` - смотрите `User`, `UserProfile`, `CommonEntity`.

## Безопасность (`_common/security`)

- Stateless JWT: `JwtAuthenticationFilter` (проверяет Bearer-токен)
- `JwtHelper` (генерация/валидация), секретный ключ в `AppProperty` / `application.yml` (`app.jwt-secret`, `app.jwt-expiration-millis`).
- `SecurityConfig`: permitAll только на `/api/v1/auth/**`, `/api/v1/info/**`, `/actuator/health/**`, swagger-пути; всё остальное - аутентификация. CSRF выключен.
- Логин: `AuthController` (`/api/v1/auth`) возвращает токен; authorities юзера собираются в `UserUtil.getAuthorities(User)` из ролей (`ROLE_<type>`) и permissions.
- Текущий юзер: `user/util/UserUtil` (`getUsername()`).

## База данных и Liquibase

- `ddl-auto: validate` - Hibernate **ничего не создаёт**, схема только из миграций.
- Master changelog: `src/main/resources/db/changelog/changelog.yml` -> далее иерархия по годам/месяцам: `26/26_changelog.yml` -> `26/08/2608_changelog.yml` -> конкретные файлы.
- Именование changeset-файлов: `YYMMDD_N_описание.yml` (например `260829_1_createUserTable.yml`), N - порядковый номер за день. Новые месяцы/годы добавляются в родительский changelog.
- Все таблицы с префиксом `school_`. Дефолтные admin-user и roles устанавливаются changeset'ом (`insertDefaultUserAndRoles`).

## Конфигурация

`src/main/resources/application.yml` - блоки профилей через `---`, профиль `DEV` по умолчанию. Смена профиля: `spring.profiles.active`.

## Ошибки API

Все исключения перехватывает `GlobalExceptionHandler` (@RestControllerAdvice) и возвращает `ErrorResponse`.
Бизнес-ошибки - `CommonException` -> HTTP 400.
Валидация `@Valid` - автоматически в `fieldErrors`.

## Тесты

- Unit-тесты сервисов: `src/test/java/.../app/unit/<domain>/service`, `Mockito` (`@ExtendWith(MockitoExtension.class)`).
- Сервис: `unit/_common/CommonServiceImplTest` - поднимает mock `EntityManager` для сервисов, наследующих `CommonService(setUpEntityManager(roleService))`.
- Фабрики мокируемых моделей: `<domain>/modelFactory/XModelFactory` (пример: `RoleModelFactory`).
- Каркас структуры: `@Nested` классы на операцию (`Create/Update/...`).

## Переменные окружения

| Переменные              | YAML ключи                   | Значение по умолчанию | Описание            |
|-------------------------|------------------------------|-----------------------|---------------------|
| `ACTIVE_PROFILE`        | `spring.profiles.active`     | `DEV`                 | Активный профиль    |
| `DB_HOST`               | `spring.datasource.url`      |                       | Хост БД             |
| `DB_PORT`               | `spring.datasource.url`      |                       | Порт БД             |
| `DB_NAME`               | `spring.datasource.url`      |                       | Название БД         |
| `DB_USERNAME`           | `spring.datasource.username` |                       | Пользватель БД      |
| `DB_PASSWORD`           | `spring.datasource.password` |                       | Пароль БД           |
| `SERVER_PORT`           | `server.port`                |                       | Порт сервера        |
| `JWT_SECRET`            | `app.jwt-secret`             |                       | Секертный ключ JWT  |
| `JWT_EXPIRATION_MILLIS` | `app.jwt-expiration-millis`  |                       | Время истечения JWT |

## Сборка и запуск

```bash
./gradlew bootRun # запуск
./gradlew test    # unit-тесты
./gradlew build   # сборка jar