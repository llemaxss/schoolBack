[Back](../../buildAndRun.md)

# Project in Docker (without monitoring)

The app and DB are started from `docker-compose.app.yml`.
The app image is built automatically from the `Dockerfile`; the `--build` flag rebuilds it after code changes.

Variables are described in [Variables for Docker and monitoring](../note/variables.md).
Monitoring for this launch: [Project in Docker + monitoring in Docker](dockerWithMonitoringRun.md).

## Method 1
Via the `.env` file:

```bash
docker compose -f docker-compose.app.yml up -d --build
```

## Method 2
Via the `.env` file if it is not located in the root of the project:

```bash
docker compose -f docker-compose.app.yml --env-file /<PATH_TO_ENV_FILE>/.env up -d --build
```

## Method 3
With specified launch parameters (they overwrite what is in `.env`, if it exists):

```bash
# Linux / macOS / Git Bash
ACTIVE_PROFILE=DEV \
DB_PORT=... \
DB_NAME=... \
... \
docker compose -f docker-compose.app.yml up -d --build
```

```powershell
# Windows PowerShell
$env:ACTIVE_PROFILE="DEV"; $env:DB_PORT="..."; $env:DB_NAME="..."; ...; docker compose -f docker-compose.app.yml up -d --build
```

## Stopping

```bash
docker compose -f docker-compose.app.yml down
```

`down` removes containers but not volumes (DB data and logs are kept).
The `-v` flag removes the volumes as well.
