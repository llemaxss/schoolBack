[Назад](../../buildAndRun.md)

# Проект в Docker + мониторинг в Docker

Приложение и БД запускаются из `docker-compose.app.yml`, мониторинг - из `docker-compose.monitoring.yml`.
Оба файла указываются через `-f`. Приложение пишет логи в том `school_app_logs`,
Alloy читает этот же том, поэтому `APP_LOGS_SOURCE=school_app_logs`.

Переменные описаны в [Переменные для Docker и мониторинга](../note/variables.md).
Параметры приложения (`.env`, `--env-file`, указание значений в командной строке)
задаются так же, как в [Проект в Docker](dockerRun.md).

## Запуск

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

Grafana откроется на `http://localhost:<GRAFANA_PORT>`, логи смотрятся так:
[Логи в Grafana](../note/grafanaLogs.md).

## Остановка

Указываются те же файлы, с которыми запускали:

```bash
docker compose -f docker-compose.app.yml -f docker-compose.monitoring.yml down
```

Команда `down` удаляет контейнеры, но не тома (данные БД, логи, данные Loki и Grafana сохраняются).
Флаг `-v` удалит и тома.
