[Назад](../README.md)

# Структура проекта

Все пакеты лежат под `com.gmail.llemaxiss.app`.

`_common` - общая инфраструктура (главная папка проекта)

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
