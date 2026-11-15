[Back](../../buildAndRun.md)

# Logs in Grafana

1. Open `http://localhost:<GRAFANA_PORT>` and sign in with `GRAFANA_ADMIN_USER` / `GRAFANA_ADMIN_PASSWORD`.
2. Select **Explore** in the menu, data source - **Loki** (added automatically).
3. Switch the query editor to **Code** mode, enter a query and click **Run query**.
4. Choose the time range at the top right.

## Labels

| Label          | Values                                    |
|----------------|-------------------------------------------|
| `service_name` | `school-management`                       |
| `level`        | `DEBUG`, `INFO`, `WARN`, `ERROR`          |
| `environment`  | value of `ACTIVE_PROFILE` (`DEV`, `PROD`) |

## Query examples

| Query                                               | Result                    |
|-----------------------------------------------------|---------------------------|
| `{service_name="school-management"}`                | all app logs              |
| `{service_name="school-management", level="ERROR"}` | errors only               |
| `{service_name="school-management"} \|= "text"`     | records containing `text` |
| `{service_name="school-management"} \| json`        | parse the JSON fields     |

## What a record contains

Each record is a JSON line with the fields
- `@timestamp`
- `log.level`
- `log.logger`
- `message`
- `process.thread.name`
- `service.name`
- `environment`

Errors with an exception also have:
- `error.type`
- `error.message`
- `error.stack_trace` (stack trace with line numbers)
