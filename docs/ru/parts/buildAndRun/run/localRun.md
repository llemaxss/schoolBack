[Назад](../../buildAndRun.md)

# Проект локально (без Docker)

Приложение запускается локально, Docker не используется: как обычное Java-приложение
(например, запуском из IDEA) или через Gradle.

Нужна доступная PostgreSQL, параметры подключения задаются переменными `DB_...`,
см. [Переменные окружения](../../environments.md).

## Запуск

Переменные окружения задаются в конфигурации запуска (например, в IDEA) или в терминале:

```bash
# Linux / macOS / Git Bash
ACTIVE_PROFILE=DEV \
DB_HOST=localhost \
DB_PORT=... \
DB_NAME=... \
... \

./gradlew bootRun         # запуск

./gradlew test            # unit-тесты
./gradlew integrationTest # integration-тесты

./gradlew build           # сборка jar
```

```powershell
# Windows (PowerShell / cmd)
$env:ACTIVE_PROFILE="DEV"
$env:DB_HOST="localhost"
$env:DB_PORT="..."
$env:DB_NAME="..."
...

.\gradlew.bat bootRun     # запуск

.\gradlew.bat test        # unit-тесты
.\gradlew integrationTest # integration-тесты

.\gradlew.bat build       # сборка jar
```

Логи пишутся в консоль и в папку `logs` в корне проекта.

Мониторинг для такого запуска: [Проект локально + мониторинг в Docker](localWithMonitoringRun.md).
