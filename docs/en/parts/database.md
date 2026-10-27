[Back](../README.md)

# Database & Liquibase

- `ddl-auto: validate` - Hibernate **never creates** anything; schema comes only from migrations.
- Master changelog: `src/main/resources/db/changelog/changelog.yml` -> year/month hierarchy: `26/26_changelog.yml` -> `26/08/2608_changelog.yml` -> concrete files.
- Changeset file naming: `YYMMDD_description.yml` (e.g. `260829_1_createUserTable.yml`), N = sequential number within the day. New months/years include in the parent changelog.
- All tables use the `school_` prefix. Default admin user and roles are set by a changeset (`insertDefaultUserAndRoles`).
