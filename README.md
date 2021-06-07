# Notes API - Ktor example

## Check env variables defaults

- Open ./src/me/porge/notes/EnvironmentParameters.kt
- Check if the default values are compatible with what you want

## Check project constants

- Open ./src/me/porge/notes/Module.kt and check if constants are compatible with what you want
- Open ./resources/application.conf and check if module path is correct

## Check Postgres

Ensure postgres is running and if the url is correct in EnvironmentParameters

## To run the project

- Ensure postgres is started and database exists.
- Run:

```bash
mvn clean && mvn package && mvn exec:java -Dexec.mainClass="io.ktor.server.netty.EngineMain"
```

## Additional info

### Mvn basics

To clean build cache:

```bash
mvn clean
```

To only update dependencies:

```bash
mvn install
```

To run tests and compile:

```bash
mvn package
```

To run only the tests:

```bash
mvn test
```

To compile:

```bash
mvn compile
```

### Postgres basics

To create a database:

```bash
psql
CREATE DATABASE notes_db;
```

To delete a database:

```bash
psql
DROP DATABASE notes_db;
```
