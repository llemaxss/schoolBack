[Back](../../buildAndRun.md)

# Project locally + monitoring in Docker

The app runs locally ([Project locally](localRun.md)), monitoring runs in Docker.
The app writes the log file to the `logs` folder in the project root and Alloy reads that folder,
so needs to set `APP_LOGS_SOURCE=./logs`.

## Starting monitoring

```bash
# Linux / macOS / Git Bash
APP_LOGS_SOURCE=./logs \
docker compose -f docker-compose.monitoring.yml up -d
```

```powershell
# Windows PowerShell
$env:APP_LOGS_SOURCE="./logs"; docker compose -f docker-compose.monitoring.yml up -d
```

The `GRAFANA_*` variables are taken from `.env`, see [Variables for Docker and monitoring](../note/variables.md).

Grafana opens at `http://localhost:<GRAFANA_PORT>`, logs are viewed as described in
[Logs in Grafana](../note/grafanaLogs.md).

## Stopping

```bash
docker compose -f docker-compose.monitoring.yml down
```

`down` removes containers but not volumes: logs in Loki and Grafana settings are kept.
The `-v` flag would remove the volumes as well.
