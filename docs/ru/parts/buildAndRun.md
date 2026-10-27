[Назад](../README.md)

# Сборка и запуск

## Локально

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

### Переменные окружения
Те что указаны в [Переменные окружения](environments.md) +

| Переменные                          | Значение по умолчанию | Описание                                |
|-------------------------------------|-----------------------|-----------------------------------------|
| _DOCKER_CONTAINER_INTERNAL_DB_PORT  | 5432                  | Внутренний порт БД в контейнере         |
| _DOCKER_CONTAINER_INTERNAL_APP_PORT | 8080                  | Внутренний порт приложения в контейнере |

Примечание: `DB_HOST` не используется как параметр, вместо этого в `docker-compose.yml` указывается наименование контейнера БД

#### Способ 1:
Через файл `.env`:

```bash
docker compose up -d --build
```

#### Способ 2:
Через файл `.env` если он лежит не в корне проекта:

```bash
docker compose --env-file /<PATH_TO_ENV_FILE>/.env up -d --build
```

#### Способ 3:
С указанаем параметров запуска (они перезапишут то что в `.env`, если он есть)

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
