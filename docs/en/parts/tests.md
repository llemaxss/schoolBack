[Back](../README.md)

# Tests

## Unit tests

- Service unit tests: `src/test/java/.../app/unit/<domain>/service`, `Mockito` (`@ExtendWith(MockitoExtension.class)`).
- Base class: `unit/_common/CommonServiceImplTest` - wires a mock `EntityManager` for services extending `CommonService(setUpEntityManager(roleService))`.
- Services which use `HibernateFilterManager`: a `@Spy` with the stub `unit/_common/HibernateFilterManagerStub` is used instead of `@Mock`.
The stub just executes the given code, so repository stubs inside it keep working (a mocked manager does not execute the code).
- Mock model factories: `<domain>/modelFactory/XModelFactory` (example: `RoleModelFactory`).
- Test structure pattern: `@Nested` classes per operation (`Create/Update/...`).
- Run: `./gradlew test` (integration tests are excluded).

## Integration tests

- Located in `src/test/java/.../app/integration`, run by a separate task: `./gradlew integrationTest`.
- Base class `integration/_common/AbstractIntegrationTest`.
- Data is cleaned after each test; rows inserted by migrations (users `admin`/`system`, roles) are kept.
- Base class helpers: `adminBearerToken()`, `bearerTokenFor(username)`, `adminUsername()`, `assignRole(username, roleType)`.
- API tests: `integration/<domain>/controller` (example: `UserControllerTest`). URLs come from the controller constants, error codes are compared through `ErrorCode.X.getId()`.
- Filter mechanism test: `integration/_common/CommonRepositoryIntegrationTest` - checks that filters work for all ways of reading through a repository and that `HibernateFilterManager` changes and restores the filter state correctly.
Its data is prepared with native SQL so that it does not depend on the code under test.
