# Cook Book

## Table of Contents

1. [Introduction](#introduction)
2. [Configuration](#configuration)
3. [Development and Running](#development-and-running)
4. [Docker](#docker)
5. [Contributing](#contributing)
6. [License](#license)

## Introduction

Cook Book is a web backend application. It provides APIs to view, query and manage recipes.

It uses

* Spring Boot 3 with WebFlux
* Kotlin with coroutines
* PostgreSQL for database
* Flyway to manage DB schemas
* R2DBC for database access
* JUnit 5 for testing

## Configuration

You should be able to run the application out-of-the-box without any further configuration (as long as database is available, see [Docker section](#docker)). However, if you wish to make your own configuration, you can do so via [application.yaml](src/main/resources/application.yaml). You can also override config values with following environment variables.

| Variable Name | Data Type | Description                          | Required                    |
|---------------|-----------|--------------------------------------|-----------------------------|
| DB_HOST       | String    | Host address of application database | No, defaults to `localhost` |
| DB_PORT       | Integer   | Port of application database         | No, defaults to `5432`      |
| DB_NAME       | String    | Name of application database         | No, defaults to `cook-book` |
| DB_USER       | String    | User of application database         | No, defaults to `cook-book` |
| DB_PASS       | String    | Password of application database     | No, defaults to `cook-book` |

## Development and Running

Cook Book is built with Gradle. Standard Gradle tasks like `clean`, `compileJava`, `compileTestJava` and `test` can be used.

If you don't have Gradle installed, you can replace `gradle` commands with `./gradlew` to use Gradle wrapper.

To run the application locally:

```bash
gradle bootRun --console=plain
```

## Docker

You can create Cook Book database using `docker-compose`:

```bash
docker-compose up -d
```

This will create database container, then you'll be able to run the application via Gradle.

Docker build can also be customized as follows:

| Variable Name | Data Type | Description                                          | Required                                |
|---------------|-----------|------------------------------------------------------|-----------------------------------------|
| DB_PORT       | Integer   | Port to bind in the host machine for the database    | No, defaults to `5432`                  |

This way, when you run the stack as following

```bash
DB_PORT=1234 docker-compose up -d
```

you'll be able to access the application the database at `jdbc:postgresql://localhost:1234/cook-book`.

## Contributing

All contributions are welcome. Please feel free to send a pull request. Thank you.

## License

Cook Book is licensed with [MIT License](LICENSE.md).
