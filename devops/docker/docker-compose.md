# Docker container operations by docker compose

## Install Docker Desktop for Window
Source: https://docs.docker.com/desktop/install/windows-install/

## Run docker-demon
Run Docker Desktop for Window for it

## Build the app in order to update jar-archive

``
mvn clean install
``

## Create the following files in classpath: 
docker-compose.yaml in order to run group of containers; 

Dockerfile - tu build docker containers for app  

## Build and run the containers in classpath

``
docker-compose up
``
