[Назад](../README.md)

# Тесты

## Unit-тесты

- Unit-тесты сервисов: `src/test/java/.../app/unit/<domain>/service`, `Mockito` (`@ExtendWith(MockitoExtension.class)`).
- Сервис: `unit/_common/CommonServiceImplTest` - поднимает mock `EntityManager` для сервисов, наследующих `CommonService(setUpEntityManager(roleService))`.
- Сервисы, использующие `HibernateFilterManager`: вместо `@Mock` берётся `@Spy` с заглушкой `unit/_common/HibernateFilterManagerStub`.
Заглушка просто выполняет переданный код, поэтому стабы репозиториев внутри него продолжают работать (мок менеджера код не выполняет).
- Фабрики мокируемых моделей: `<domain>/modelFactory/XModelFactory` (пример: `RoleModelFactory`).
- Каркас структуры: `@Nested` классы на операцию (`Create/Update/...`).
- Запуск: `./gradlew test` (интеграционные тесты исключены).

## Интеграционные тесты

- Лежат в `src/test/java/.../app/integration`, запускаются отдельной задачей: `./gradlew integrationTest`.
- Базовый класс `integration/_common/AbstractIntegrationTest`.
- После каждого теста данные чистятся, строки, добавленные миграциями (пользователи `admin`/`system`, роли), остаются.
- Помощники базового класса: `adminBearerToken()`, `bearerTokenFor(username)`, `adminUsername()`, `assignRole(username, roleType)`.
- Тесты API: `integration/<domain>/controller` (пример: `UserControllerTest`). URL берутся из констант контроллеров, коды ошибок сравниваются через `ErrorCode.X.getId()`.
- Тест механизма фильтров: `integration/_common/CommonRepositoryIntegrationTest` - проверяет, что фильтры работают для всех способов чтения репозитория и что `HibernateFilterManager` правильно меняет и возвращает состояние фильтров.
Данные для него готовятся нативным SQL, чтобы не зависеть от проверяемого кода.
