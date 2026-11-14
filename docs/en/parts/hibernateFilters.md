[Back](../README.md)

# Hibernate filters

(`_common/hibernateFilter`, `user/hibernateFilter`)

Query filtering at the Hibernate level. Two filters exist:
- **soft delete** - based on the `delete_ts` column, applies to all entities (`CommonEntity`);
- **active users** - based on the `is_active` column, applies to `User` only.

## How it works

1. Each filter is declared with `@FilterDef` in `package-info.java` (common ones in `_common/hibernateFilter`, user ones in `user/hibernateFilter`) with `autoEnabled = true`.
2. The filter parameter has a `resolver` - a `Supplier` class which returns the default value.
3. Hibernate enables such filters itself when any session is created.
4. The parameter value is taken in this order: the value set explicitly first, then the value from the `resolver`.

As a result, deleted records and inactive users are invisible everywhere by default: in web requests, in repository tests and in background jobs.

## How to change filters from code

Only through `component/HibernateFilterManager`. There are three methods per filter, and each takes the code (`Supplier`) to execute.

```java
Object result = hibernateFilterManager.executeXXX(() -> {
  return someFunction(params);
});
```

Rules:
- A filter cannot be enabled or disabled "for good". It is changed only while the given code runs; after it (even if the code throws an exception) the state from before the call is restored.
So nested calls work correctly: the inner one returns the filter to the state of the outer one.
- Wrap the **smallest** piece of code (usually one repository call). Everything called inside (including other services) sees the changed filter too.
- The manager works only inside a transaction. Outside a transaction Spring gives a temporary session and the changes are lost.
- To restore the previous parameter value the manager reads it through the internal Hibernate class `FilterImpl`.

## What filters do not cover

Hibernate does not apply filters to direct loading by primary key (`em.find`, `getReferenceById`, lazy `@ManyToOne` associations) and to native queries. Therefore:
- `CommonRepositoryImpl.findById` is overridden and goes through a query (`findOne(Specification)`) so that the filters work.
- The conditions on `delete_ts` and `is_active` are written by hand in native queries .
- Filters apply only where `@Filter(...)` is declared on the entity or association - see `CommonEntity`, `User`.

## How to add a new filter

1. Constants (name, parameter name, SQL condition) - in `util/*HibernateFilterConstants` of the module.
2. A `Supplier<Boolean>` class with the default value.
3. `@FilterDef` in `package-info.java` with `resolver = ...` and `autoEnabled = true`.
4. `@Filter(name = ...)` on the entity (or association).
5. Three `execute...` methods in `HibernateFilterManager` (like the existing ones).
