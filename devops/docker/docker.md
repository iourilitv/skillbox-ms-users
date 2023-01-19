# Docker container operations
Source: https://www.youtube.com/watch?v=Sa7uOGczoHc

## Install Docker Desktop for Window
Source: https://docs.docker.com/desktop/install/windows-install/

## Run docker-demon
Run Docker Desktop for Window for it

## NOT REQUIRED! Pull required images from hub.docker.com
### Pull image PostgreSQL
Source: https://hub.docker.com/_/postgres

``
docker pull postgres:14.6
``

## Run container with database PostgreSQL
The image postgres version 14.6 will be downloaded from hub.docker.com automatically instead of to pull it directly

To save data state use the following arg: -v ./pg_data:/var/lib/postgresql/data/pgdata

``
docker run -d --name users-postgres-db -e POSTGRES_DB=users -e POSTGRES_USER=microuser -e POSTGRES_PASSWORD=microuser -v ./pg_data:/var/lib/postgresql/data/pgdata -p 54321:5432 postgres:14.6
``

## Run container with pgAdmin
### Source: https://hub.docker.com/r/dpage/pgadmin4. 
### Image: dpage/pgadmin4 version: 6.9
``
docker run -d --name users-pgadmin -e PGADMIN_DEFAULT_EMAIL=notexists@mail.com -e PGADMIN_DEFAULT_PASSWORD=admin -p 8888:80 dpage/pgadmin4:6.9
``

Then open pgAdmin in a browser

## Creating network to relate containers of database and pgAdmin
### Creating a new network
``
docker network create users-db-net
``
### Connect containers to the network
``
docker network connect users-db-net users-postgres-db
docker network connect users-db-net users-pgadmin
``
### Then open pgAdmin in a browser and login
url: localhost:8888
Email Address / Username: notexists@mail.com
Password: admin
#### Register a new Server using data from container users-postgres-db
##### General tab
Name: users-postgres-db
##### Connection tab
Host name/address: users-postgres-db
Port: 5432
Maintenance database: users
Username: microuser
Password: microuser

#### Create schemas:
users_scheme; users_test_scheme

## Creating docker container for app
### Create a file Dockerfile with instructions for the container building
The file location: project classpath
### Build image for app
-t - image name; 
"." means context in the project classpath

``
docker build -t users-app .
``

### Run container
``
docker run -d --name users-app -p 9010:9010 users-app
``

### Connect app container to the network
``
docker network connect users-db-net users-app
``