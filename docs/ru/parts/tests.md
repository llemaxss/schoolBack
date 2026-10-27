[Назад](../README.md)

# Тесты

- Unit-тесты сервисов: `src/test/java/.../app/unit/<domain>/service`, `Mockito` (`@ExtendWith(MockitoExtension.class)`).
- Сервис: `unit/_common/CommonServiceImplTest` - поднимает mock `EntityManager` для сервисов, наследующих `CommonService(setUpEntityManager(roleService))`.
- Фабрики мокируемых моделей: `<domain>/modelFactory/XModelFactory` (пример: `RoleModelFactory`).
- Каркас структуры: `@Nested` классы на операцию (`Create/Update/...`).
