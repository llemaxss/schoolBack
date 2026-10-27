[Назад](../README.md)

# Hibernate filters

(`_common/hibernateFilter`)

Кастомный механизм фильтрации запросов на уровне Hibernate. Два основных фильтра:
- **soft-delete** (`SOFT_DELETE_FILTER_NAME`) - по колонке `delete_ts`;
- **active users** (`USER_ACTIVE_FILTER_NAME`) - по колонке `is_active` (только активные `User`).

Как это работает:
- `config/HibernateFilterConfig` регистрирует `component/HibernateFilterInterceptor` как Hibernate interceptor.
- Interceptor в `afterTransactionBegin` **автоматически включает на каждую транзакцию**: «только неудалённые» + «только активные пользователи». То есть по умолчанию удалённые/деактивированные записи не видны вообще.
- `component/HibernateFilterManager` - программное управление: `enableDeletedOnlyFilter()`, `disableSoftDeleteFilter()` и т.п. (например, чтобы админ увидел удалённых пользователей). Работает через `ObjectProvider<EntityManager>` - так разорвана циклическая зависимость Config->Interceptor->Manager->EntityManager.
- Сами `@FilterDef` (имена, параметры, SQL-условия) объявлены в `package-info.java`: общие - в `_common/hibernateFilter`. Константы - в `util/HibernateFilterConstants`. Новый фильтр = новый `@FilterDef` в package-info + константа + метод в Manager + `@Filter` на сущности.

Внимание: фильтры действуют только там, где на entity/ассоциации висит `@Filter(...)` - смотрите `User`, `UserProfile`, `CommonEntity`.
