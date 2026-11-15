[Back](../../buildAndRun.md)

# Run and monitoring files

## Project root

| File                            | Purpose                                                          |
|---------------------------------|------------------------------------------------------------------|
| `Dockerfile`                    | Build of the app image                                           |
| `docker-compose.app.yml`        | App and DB: services `db` and `app`                              |
| `docker-compose.monitoring.yml` | Log monitoring: services `loki`, `grafana` and `alloy`           |
| `.env`                          | Template of environment variables (values are filled in locally) |

The compose files are independent: `docker-compose.monitoring.yml` can be started alone or together
with `docker-compose.app.yml` (both files are passed via `-f`, see [Project in Docker + monitoring in Docker](../run/dockerWithMonitoringRun.md)).

## `monitoring` folder

| File                                                    | Purpose                                                 |
|---------------------------------------------------------|---------------------------------------------------------|
| `monitoring/loki/loki-config.yaml`                      | Loki settings: on-disk storage, 30-day retention        |
| `monitoring/alloy/config.alloy`                         | Alloy settings: reads the log file and sends it to Loki |
| `monitoring/grafana/provisioning/datasources/loki.yaml` | Automatically adds Loki as a Grafana data source        |

## Log flow

```text
app -> logs/school-management.log (JSON) -> Alloy -> Loki -> Grafana
```

- The app writes logs to the console and to the `school-management.log` file (JSON, ECS format).
- Alloy reads the JSON file, takes the record time and level from it, adds the labels
  `service_name`, `level`, `environment` and sends the records to Loki.
- Loki keeps logs for 30 days. Loki ports are not published; Grafana and Alloy reach it
  inside the Docker network.
- Grafana shows the logs; the Loki data source is added automatically.

More on viewing logs: [Logs in Grafana](grafanaLogs.md).

## Containers

| Container        | Service   | Compose file                    |
|------------------|-----------|---------------------------------|
| `school_db`      | `db`      | `docker-compose.app.yml`        |
| `school_app`     | `app`     | `docker-compose.app.yml`        |
| `school_loki`    | `loki`    | `docker-compose.monitoring.yml` |
| `school_grafana` | `grafana` | `docker-compose.monitoring.yml` |
| `school_alloy`   | `alloy`   | `docker-compose.monitoring.yml` |

## Docker volumes

Docker prefixes every volume name with the project name (by default the name of the folder
with the compose file), for example `back_school_app_logs`.

| Volume                | Data                                    | Declared in                     |
|-----------------------|-----------------------------------------|---------------------------------|
| `school_db_data`      | PostgreSQL data                         | `docker-compose.app.yml`        |
| `school_app_logs`     | app log files (`/app/logs`)             | `docker-compose.app.yml`        |
| `school_app_data`     | app data (`/app/data`)                  | `docker-compose.app.yml`        |
| `school_loki_data`    | Loki storage                            | `docker-compose.monitoring.yml` |
| `school_grafana_data` | Grafana data                            | `docker-compose.monitoring.yml` |
| `school_alloy_data`   | Alloy service data (positions in files) | `docker-compose.monitoring.yml` |

The `school_app_logs` volume is also listed in `docker-compose.monitoring.yml`: when both files are
started together it is the same volume, used by the app for writing and by Alloy for reading.

## App log files

| File                                  | Contents                                                                             |
|---------------------------------------|--------------------------------------------------------------------------------------|
| `school-management.log`               | All logs in JSON                                                                     |
| `school-management-error.log`         | Errors only (`ERROR`), plain text                                                    |
| `school-management-<date>.<n>.log.gz` | Archive: the file rotates daily or at 50 MB, archives older than 30 days are deleted |

Logs are stored in the `logs` folder (the path is set by the `LOG_PATH` system property, default `logs` relative to the working directory).
In Docker it is `/app/logs`.
