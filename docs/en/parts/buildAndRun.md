[Back](../README.md)

# Build & run

## Locally

```bash
# Linux / macOS / Git Bash
ACTIVE_PROFILE=DEV \
DB_HOST=localhost \
DB_PORT=... \
DB_NAME=... \
... \

./gradlew bootRun # запуск

./gradlew test    # unit-тесты

./gradlew build   # сборка jar
```

```powershell
# Windows (PowerShell / cmd)
$env:ACTIVE_PROFILE="DEV"
$env:DB_HOST="localhost"
$env:DB_PORT="..."
$env:DB_NAME="..."
...

.\gradlew.bat bootRun # запуск

.\gradlew.bat test    # unit-тесты

.\gradlew.bat build   # сборка jar
```

## Docker

### Environment variables
Listed in [Environment variables](environments.md) +

| Variable                            | Default value  | Description                           |
|-------------------------------------|----------------|---------------------------------------|
| _DOCKER_CONTAINER_INTERNAL_DB_PORT  | 5432           | Internal port of the DB in container  |
| _DOCKER_CONTAINER_INTERNAL_APP_PORT | 8080           | Internal port of the App in container |

Note: `DB_HOST` is not used as a parameter, instead the name of the DB container is specified in `docker-compose.yml`

#### Method 1:
Via `.env` file:

```bash
docker compose up -d --build
```

#### Method 2:
Via the `.env` file if it is not located in the root of the project:

```bash
docker compose --env-file /<PATH_TO_ENV_FILE>/.env up -d --build
```

#### Method 3:
With specified the launch parameters (they will overwrite what is in `.env`, if it exists)

````bash
# Linux / macOS / Git Bash
ACTIVE_PROFILE=DEV \
DB_PORT=... \
DB_NAME=... \
... \
docker compose up -d --build
````

````powershell
# Windows PowerShell
$env:ACTIVE_PROFILE="DEV"; $env:DB_PORT="..."; $env:DB_NAME="..."; ...; docker compose up -d --build
````
