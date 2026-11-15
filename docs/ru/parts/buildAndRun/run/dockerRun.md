[Назад](../../buildAndRun.md)

# Проект в Docker (без мониторинга)

Приложение и БД запускаются из `docker-compose.app.yml`.
Образ приложения собирается по `Dockerfile` автоматически, флаг `--build` пересобирает его при изменениях кода.

Переменные описаны в [Переменные для Docker и мониторинга](../note/variables.md).
Мониторинг для такого запуска: [Проект в Docker + мониторинг в Docker](dockerWithMonitoringRun.md).

## Способ 1
Через файл `.env`:

```bash
docker compose -f docker-compose.app.yml up -d --build
```

## Способ 2
Через файл `.env`, если он лежит не в корне проекта:

```bash
docker compose -f docker-compose.app.yml --env-file /<PATH_TO_ENV_FILE>/.env up -d --build
```

## Способ 3
С указанием параметров запуска (они перезапишут то, что в `.env`, если он есть):

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

## Остановка

```bash
docker compose -f docker-compose.app.yml down
```

Команда `down` удаляет контейнеры, но не тома (данные БД и логи сохраняются).
Флаг `-v` удалит и тома.
