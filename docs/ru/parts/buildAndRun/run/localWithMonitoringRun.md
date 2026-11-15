[Назад](../../buildAndRun.md)

# Проект локально + мониторинг в Docker

Приложение запускается локально ([Проект локально](localRun.md)), мониторинг - в Docker.
Приложение пишет файл логов в папку `logs` в корне проекта, Alloy читает эту папку,
поэтому нужно указать `APP_LOGS_SOURCE=./logs`.

## Запуск мониторинга

```bash
# Linux / macOS / Git Bash
APP_LOGS_SOURCE=./logs \
docker compose -f docker-compose.monitoring.yml up -d
```

```powershell
# Windows PowerShell
$env:APP_LOGS_SOURCE="./logs"; docker compose -f docker-compose.monitoring.yml up -d
```

Переменные `GRAFANA_*` берутся из `.env`, см. [Переменные для Docker и мониторинга](../note/variables.md).

Grafana откроется на `http://localhost:<GRAFANA_PORT>`, логи смотрятся так:
[Логи в Grafana](../note/grafanaLogs.md).

## Остановка

```bash
docker compose -f docker-compose.monitoring.yml down
```

Команда `down` удаляет контейнеры, но не тома: логи в Loki и настройки Grafana сохраняются.
Флаг `-v` удалил бы и тома.
