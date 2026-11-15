[Back](../../buildAndRun.md)

# Project locally (without Docker)

The app runs locally without Docker: as a regular Java application
(for example, started from IDEA) or via Gradle.

An available PostgreSQL is required; the connection parameters are set with the variables `DB_...`,
see [Environment variables](../../environments.md).

## Run

Environment variables are set in the run configuration (for example, in IDEA) or in the terminal:

```bash
# Linux / macOS / Git Bash
ACTIVE_PROFILE=DEV \
DB_HOST=localhost \
DB_PORT=... \
DB_NAME=... \
... \

./gradlew bootRun         # run

./gradlew test            # unit tests
./gradlew integrationTest # integration tests

./gradlew build           # build jar
```

```powershell
# Windows (PowerShell / cmd)
$env:ACTIVE_PROFILE="DEV"
$env:DB_HOST="localhost"
$env:DB_PORT="..."
$env:DB_NAME="..."
...

.\gradlew.bat bootRun     # run

.\gradlew.bat test        # unit tests
.\gradlew integrationTest # integration tests

.\gradlew.bat build       # build jar
```

Logs are written to the console and to the `logs` folder in the project root.

Monitoring for this launch: [Project locally + monitoring in Docker](localWithMonitoringRun.md).
