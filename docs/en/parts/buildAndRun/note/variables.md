[Back](../../buildAndRun.md)

# Variables for Docker and monitoring

App variables are described in [Environment variables](../../environments.md). Only the ones needed for
Docker and monitoring are listed here. Values are set in the `.env` file, in the environment
or in the launch parameters (they overwrite `.env`).

| Variable                                  | Default value | Description                                     |
|-------------------------------------------|---------------|-------------------------------------------------|
| `_DOCKER_CONTAINER_INTERNAL_DB_PORT`      | `5432`        | Internal port of the DB in the container        |
| `_DOCKER_CONTAINER_INTERNAL_APP_PORT`     | `8080`        | Internal port of the app in the container       |
| `_DOCKER_CONTAINER_INTERNAL_GRAFANA_PORT` | `3000`        | Internal port of Grafana in the container       |
| `GRAFANA_PORT`                            |               | Port on which Grafana is available locally      |
| `GRAFANA_ADMIN_USER`                      |               | Grafana administrator login                     |
| `GRAFANA_ADMIN_PASSWORD`                  |               | Grafana administrator password                  |
| `APP_LOGS_SOURCE`                         |               | Where Alloy reads the app logs from (see below) |

`DB_HOST` is not used in Docker: the name of the DB container is specified in `docker-compose.app.yml` instead.

## `APP_LOGS_SOURCE`

Required when starting `docker-compose.monitoring.yml`; an empty value causes an error.

| Value             | When to use                                                                                   |
|-------------------|-----------------------------------------------------------------------------------------------|
| `./logs`          | The app is started locally (not in Docker): Alloy reads the `logs` folder in the project root |
| `school_app_logs` | The app is started in Docker: Alloy reads the volume with the app logs                        |

The variable is left empty in `.env`; the value is set at launch
(see [Project locally + monitoring in Docker](../run/localWithMonitoringRun.md) and
[Project in Docker + monitoring in Docker](../run/dockerWithMonitoringRun.md)).
In an IDEA run configuration the value is set in the Environment variables field.
