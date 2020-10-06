# How to use Testcontainers
## Intro

This is a Spring Boot application used as an example of how to use Testcontainers in the project and its integration tests. 
It showcases you how to migrate from a embedded H2 database in your integration tests and instead use a real MariaDb database. 

This project consists of several directories that contain the application both before and after the changes. 

We have two folders, origin and solution.
* the [origin](origin) directory contains the source code before we migrated our tests from H2 to Testcontainers.
* the [solution](solution) directory contains the source code after we migrated our tests from H2 to Testcontainers.
* the [faster](faster) directory contains the source code when doing some changes to make our tests run faster

## License

This project is licensed under the terms of the [Apache License, Version 2.0](LICENSE).
