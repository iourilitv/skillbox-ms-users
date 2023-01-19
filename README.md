# Users microservice

As part of my educational project for the course "Microservices Architecture" in Skillbox.
Since 21.11.2022

## Stack of Technologies
### MapStruct with Lombok annotations generating
Sources:
Quick Guide to MapStruct https://www.baeldung.com/mapstruct;
Что такое Mapstruct и как правильно настроить его для модульного тестирования в SpringBoot приложениях https://javarush.com/groups/posts/3698-chto-takoe-mapstruct-i-kak-praviljhno-nastroitjh-ego-dlja-moduljhnogo-testirovanija-v-springboo

### OpenAPI
Sources:
Documenting a Spring REST API Using OpenAPI 3.0 https://www.baeldung.com/spring-rest-openapi-documentation.

## Installation
### default profile. With only unit test executing.
Unit test names end with Test.
Use the following command: mvn clean install

### integration-test profile. With only integration tests executing.
Integration test names start with ITest.
Use the following command: mvn verify -Pintegration-test

## Configuring

```
Add instructions to configue example-ishop-spring app
```

## Usage

```
Add instructions to run example-ishop-spring app
```

## OpenApi of the app is acceptable in a browser
In browser: http://192.168.59.102:30001/swagger-ui/index.html


## DevOps
### Docker containers using instructions is located at devops/docker.md
### Instructions of how to run docker containers with docker-compose.yaml using is located at devops/docker-compose.md
### Instructions of how to execute module and integration tests with testcontainers and docker using is located in testcontainers-with-docker.md    
### Instructions of how to run docker containers with kubernetes is located in devops/K8s folder


## Should be added
### 1. DB Audit
В сущности добавить поля, сохраняющие информацию о создании и последнем обновлении сущности.
Можно средствами аудита:
https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#events-jpa-callbacks
https://www.baeldung.com/database-auditing-jpa
Можно проще - обычно это поля в таблице. Назовем их last_update_date_time и created_date_time. 
Их можно заполнить с помощью аннотаций @CreationTimestamp и @UpdateTimestamp. 
Или с помощью функций помеченными аннотациями @PrePersist и @PreUpdate. 

### 2. Separate DTOs to two parts: for requests and for response
Better is to separate them by their functional requirements

## Maintainers
- Iury Litvinenko
