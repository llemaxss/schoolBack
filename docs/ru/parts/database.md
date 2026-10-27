[Назад](../README.md)

# База данных и Liquibase

- `ddl-auto: validate` - Hibernate **ничего не создаёт**, схема только из миграций.
- Master changelog: `src/main/resources/db/changelog/changelog.yml` -> далее иерархия по годам/месяцам: `26/26_changelog.yml` -> `26/08/2608_changelog.yml` -> конкретные файлы.
- Именование changeset-файлов: `YYMMDD_N_описание.yml` (например `260829_1_createUserTable.yml`), N - порядковый номер за день. Новые месяцы/годы добавляются в родительский changelog.
- Все таблицы с префиксом `school_`. Дефолтные admin-user и roles устанавливаются changeset'ом (`insertDefaultUserAndRoles`).
