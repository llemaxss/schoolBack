[Back](../../buildAndRun.md)

# Project in Docker + monitoring in Docker

The app and DB are started from `docker-compose.app.yml`, monitoring from `docker-compose.monitoring.yml`.
Both files are passed via `-f`. The app writes logs to the `school_app_logs` volume and
Alloy reads the same volume, so `APP_LOGS_SOURCE=school_app_logs`.

Variables are described in [Variables for Docker and monitoring](../note/variables.md).
The app parameters (`.env`, `--env-file`, values in the command line)
are set the same way as in [Project in Docker](dockerRun.md).

## Starting

```bash
# Linux / macOS / Git Bash
APP_LOGS_SOURCE=school_app_logs \
docker compose \
  -f docker-compose.app.yml \
  -f docker-compose.monitoring.yml \
  up -d --build
```

```powershell
# Windows PowerShell
$env:APP_LOGS_SOURCE="school_app_logs"; docker compose -f docker-compose.app.yml -f docker-compose.monitoring.yml up -d --build
```

Grafana opens at `http://localhost:<GRAFANA_PORT>`, logs are viewed as described in
[Logs in Grafana](../note/grafanaLogs.md).

## Stopping

Pass the same files that were used for starting:

```bash
docker compose -f docker-compose.app.yml -f docker-compose.monitoring.yml down
```

`down` removes containers but not volumes (DB data, logs, Loki and Grafana data are kept).
The `-v` flag removes the volumes as well.
