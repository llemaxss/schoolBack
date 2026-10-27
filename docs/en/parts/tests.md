[Back](../README.md)

# Tests

- Service unit tests: `src/test/java/.../app/unit/<domain>/service`, `Mockito` (`@ExtendWith(MockitoExtension.class)`).
- Base class: `unit/_common/CommonServiceImplTest` - wires a mock `EntityManager` for services extending `CommonService(setUpEntityManager(roleService))`.
- Mock model factories: `<domain>/modelFactory/XModelFactory` (example: `RoleModelFactory`).
- Test structure pattern: @Nested classes per operation (`Create/Update/...`).
