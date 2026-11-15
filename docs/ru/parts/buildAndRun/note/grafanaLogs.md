[Назад](../../buildAndRun.md)

# Логи в Grafana

1. Открыть `http://localhost:<GRAFANA_PORT>` и войти с `GRAFANA_ADMIN_USER` / `GRAFANA_ADMIN_PASSWORD`.
2. В меню выбрать **Explore**, источник данных - **Loki** (подключён автоматически).
3. Переключить редактор запроса в режим **Code**, ввести запрос и нажать **Run query**.
4. Выбрать нужный диапазон времени справа вверху.

## Метки

| Метка          | Значения                                  |
|----------------|-------------------------------------------|
| `service_name` | `school-management`                       |
| `level`        | `DEBUG`, `INFO`, `WARN`, `ERROR`          |
| `environment`  | значение `ACTIVE_PROFILE` (`DEV`, `PROD`) |

## Примеры запросов

| Запрос                                              | Результат                 |
|-----------------------------------------------------|---------------------------|
| `{service_name="school-management"}`                | все логи приложения       |
| `{service_name="school-management", level="ERROR"}` | только ошибки             |
| `{service_name="school-management"} \|= "text"`     | записи, содержащие `text` |
| `{service_name="school-management"} \| json`        | разбор JSON-полей записи  |

## Что видно в записи

Каждая запись - это JSON-строка с полями:
- `@timestamp`
- `log.level`
- `log.logger`
- `message`
- `process.thread.name`
- `service.name`
- `environment`

Для ошибок с исключением добавляются:
- `error.type`
- `error.message`
- `error.stack_trace` (стек с номерами строк)
