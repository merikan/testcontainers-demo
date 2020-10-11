# How to use Testcontainers
## Intro

This is a Spring Boot application used as an example in my blogs posts, how to use [Testcontainers](https://www.testcontainers.org/) in a [Spring Boot](https://spring.io/projects/spring-boot) project and 
its integration tests. It shows you how to migrate from an embedded h2 database to a real Mariadb database running 
in a docker container. After that, how you can get your tests to start much faster.

Read more in my blog posts
* [Getting started with Testcontainers](https://callistaenterprise.se/blogg/teknik/2020/10/08/getting-started-with-testcontainers/) ([alternative](https://merikan.com/2020/10/getting-started-with-testcontainers/))
* [Speed-up your Testcontainers tests](https://callistaenterprise.se/blogg/teknik/2020/10/09/speed-up-your-testcontainers-tests/) ([alternative](https://merikan.com/2020/10/speed-up-your-testcontainers-tests/))

## The source code
This project consists of several directories that contain the application both before and after the changes. 

The following directories are available
* the [origin](origin) directory contains the source code *before* we migrated our tests from H2 to Testcontainers.
* the [solution](solution) directory contains the source code *after* we migrated our tests from H2 to Testcontainers.
* the [faster](faster) directory contains the source code after doing some changes to make our tests run faster

## How to run the code
1. Check out the code
```
$ git clone https://github.com/merikan/testcontainers-demo.git
```
2. cd into the desired directory
```
$ cd testcontainers-demo/faster
```
3. and run the tests
```
$ ./mvnw clean test verify
```

## License

This project is licensed under the terms of the [Apache License, Version 2.0](LICENSE).
