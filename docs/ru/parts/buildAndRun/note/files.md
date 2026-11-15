[Назад](../../buildAndRun.md)

# Файлы запуска и мониторинга

## Корень проекта

| Файл                            | Назначение                                                  |
|---------------------------------|-------------------------------------------------------------|
| `Dockerfile`                    | Сборка образа приложения                                    |
| `docker-compose.app.yml`        | Приложение и БД: сервисы `db` и `app`                       |
| `docker-compose.monitoring.yml` | Мониторинг логов: сервисы `loki`, `grafana` и `alloy`       |
| `.env`                          | Шаблон переменных окружения (значения заполняются локально) |

Файлы compose независимы: `docker-compose.monitoring.yml` можно запускать отдельно или вместе
с `docker-compose.app.yml` (оба файла указываются через `-f`, см. [Проект в Docker + мониторинг в Docker](../run/dockerWithMonitoringRun.md)).

## Папка `monitoring`

| Файл                                                    | Назначение                                                |
|---------------------------------------------------------|-----------------------------------------------------------|
| `monitoring/loki/loki-config.yaml`                      | Настройки Loki: хранение на диске, срок хранения 30 дней  |
| `monitoring/alloy/config.alloy`                         | Настройки Alloy: чтение файла логов и отправка в Loki     |
| `monitoring/grafana/provisioning/datasources/loki.yaml` | Автоматически подключает Loki как источник данных Grafana |

## Как идут логи

```text
приложение -> logs/school-management.log (JSON) -> Alloy -> Loki -> Grafana
```

- Приложение пишет логи в консоль и в файл `school-management.log` (JSON, формат ECS).
- Alloy читает JSON-файл, берёт из него время записи и уровень, добавляет метки
  `service_name`, `level`, `environment` и отправляет записи в Loki.
- Loki хранит логи 30 дней. Порты Loki наружу не опубликованы, к нему обращаются Grafana и Alloy
  внутри сети Docker.
- Grafana показывает логи, источник данных Loki подключается автоматически.

Подробнее о просмотре логов: [Логи в Grafana](grafanaLogs.md).

## Контейнеры

| Контейнер        | Сервис    | Файл compose                    |
|------------------|-----------|---------------------------------|
| `school_db`      | `db`      | `docker-compose.app.yml`        |
| `school_app`     | `app`     | `docker-compose.app.yml`        |
| `school_loki`    | `loki`    | `docker-compose.monitoring.yml` |
| `school_grafana` | `grafana` | `docker-compose.monitoring.yml` |
| `school_alloy`   | `alloy`   | `docker-compose.monitoring.yml` |

## Тома Docker

К имени каждого тома Docker добавляет имя проекта (по умолчанию - имя папки с compose-файлом),
например `back_school_app_logs`.

| Том                   | Данные                                    | Где объявлен                    |
|-----------------------|-------------------------------------------|---------------------------------|
| `school_db_data`      | данные PostgreSQL                         | `docker-compose.app.yml`        |
| `school_app_logs`     | файлы логов приложения (`/app/logs`)      | `docker-compose.app.yml`        |
| `school_app_data`     | данные приложения (`/app/data`)           | `docker-compose.app.yml`        |
| `school_loki_data`    | хранилище Loki                            | `docker-compose.monitoring.yml` |
| `school_grafana_data` | данные Grafana                            | `docker-compose.monitoring.yml` |
| `school_alloy_data`   | служебные данные Alloy (позиции в файлах) | `docker-compose.monitoring.yml` |

Том `school_app_logs` указан и в `docker-compose.monitoring.yml`: при запуске обоих файлов
вместе это один и тот же том, который приложение использует для записи, а Alloy - для чтения.

## Файлы логов приложения

| Файл                                  | Содержимое                                                                                   |
|---------------------------------------|----------------------------------------------------------------------------------------------|
| `school-management.log`               | Все логи в формате JSON                                                                      |
| `school-management-error.log`         | Только ошибки (`ERROR`), обычный текст                                                       |
| `school-management-<дата>.<n>.log.gz` | Архив: файл ротируется раз в сутки или при достижении 50 МБ, архивы старше 30 дней удаляются |

Логи лежат в папке `logs` (путь задаётся системным свойством `LOG_PATH`, по умолчанию `logs` относительно папки запуска).
В Docker это `/app/logs`.
