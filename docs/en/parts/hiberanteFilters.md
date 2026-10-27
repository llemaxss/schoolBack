[Back](../README.md)

# Hibernate filters

(`_common/hibernateFilter`)

Custom query-filtering mechanism at the Hibernate level. Two main filters exist:
- **soft delete** (`SOFT_DELETE_FILTER_NAME`) - based on the `delete_ts` column;
- **active users** (`USER_ACTIVE_FILTER_NAME`) - based on the `is_active` column (active `User` only).

How it works:
- `config/HibernateFilterConfig` registers `component/HibernateFilterInterceptor` as a Hibernate interceptor.
- The interceptor in `afterTransactionBegin` **automatically enables for every transaction**: "not deleted only" + "active users only". By default, deleted/deactivated records are simply invisible.
- `component/HibernateFilterManager` - programmatic control: `enableDeletedOnlyFilter()`, `disableSoftDeleteFilter()`, etc. (e.g. so an admin can see deleted users). It uses `ObjectProvider<EntityManager>` deliberately - this breaks the circular dependency Config->Interceptor->Manager->EntityManager.
- The `@FilterDef`s themselves (names, parameters, SQL conditions) are declared in `package-info.java`: common ones in `_common/hibernateFilter`. Constants live in `util/HibernateFilterConstants`. A new filter = new `@FilterDef` in package-info + constant + Manager method + `@Filter` on the entity.

Note: filters only apply where `@Filter(...)` is declared on the entity/association - see `User`, `UserProfile`, `CommonEntity`.
