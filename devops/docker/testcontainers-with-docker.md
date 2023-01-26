# Testcontainers with docker
Instructions of how to execute module and integration tests with testcontainers and docker using

## Example sources.
Тестирование с помощью Testcontainers: как поднять в контейнере тестовую базу: 
https://sysout.ru/testirovanie-s-pomoshhyu-testcontainers-ili-kak-podnyat-v-kontejnere-testovuyu-bazu/

DB Integration Tests with Spring Boot and Testcontainers:
https://www.baeldung.com/spring-boot-testcontainers-integration-test

## Notice.
### The tests work but with the following comment in logs:

14:19:13.684 [main] DEBUG com.github.dockerjava.zerodep.ApacheDockerHttpClientImpl$ApacheResponse - Failed to close the response
java.io.IOException: java.nio.channels.ClosedChannelException

More details in ./devops/docker/logs/testcontainer-tests.log

