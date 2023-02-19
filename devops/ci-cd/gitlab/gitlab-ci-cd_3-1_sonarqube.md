# Creating pipeline on GitLab. Part 3-1. "sonarqube-check" stage (sonarqube for Pull Requests)
Installing and Running docker container with SonarQube server by Docker image.           
Sources:        
GitLab integration: https://docs.sonarqube.org/latest/devops-platform-integration/gitlab-integration/       
SonarScanner for Maven: https://docs.sonarqube.org/9.6/analyzing-source-code/scanners/sonarscanner-for-maven/       
SonarSource/sonar-scanning-examples/...: https://github.com/SonarSource/sonar-scanning-examples/blob/master/sonarqube-scanner-maven/maven-basic/pom.xml          
Контролируем качество кода с помощью SonarQube(In IDEA. Code 35:28; Plugin SonarLint 44:13): https://www.youtube.com/watch?v=fDqDeLkILoI
Successfully.

## Prepare environment. Do operations if still have not done
### Install Docker Desktop for Window
Source: https://docs.docker.com/desktop/install/windows-install/
### Run docker-demon
Run Docker Desktop for Window for it
### Run PowerShell (PS) as administrator
### In PS go to D:\Software\gitlab\GitLab-Runner
``
cd D:\Software\gitlab\GitLab-Runner
``

Then working in PS D:\Software\gitlab\GitLab-Runner>

### Start default gitlab runner with PS: gitlab-win64-local
``
./gitlab-runner.exe start
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe start
Runtime platform                                    arch=amd64 os=windows pid=3280 revision=12335144 version=15.8.0
``

### Start Docker Desktop for integration tests that use testcontainers
In Windows Start

## Create SonarQube as a server
Sources:         
Installing SonarQube from the Docker image: https://docs.sonarqube.org/latest/setup-and-upgrade/install-the-server/                     
Images source: https://hub.docker.com/_/sonarqube

### Pull the image into a local docker manually using docker engine (not necessary)
Using sonarqube:community with "latest" tag.          
``
docker pull sonarqube:community
``

Result:        
``
...
6651edf80baa: Pull complete
Digest: sha256:25e1433817b1d044b9ec7cd37cf070f4ed3131dfa8cf8a89a08928b693f97eff
Status: Downloaded newer image for sonarqube:community
docker.io/library/sonarqube:community
``

### Run Sonarqube docker container
#### Easy way without volumes, not recommended
The image is pulling automatically if not exist in local docker.            
There:    
--name {app name inside container}      
-p {container's port}:{app's port}
--stop-timeout {time(sec) to keep the container alive - docker could stop after 10 secs}
``
docker run -d --name sonarqube -p 9000:9000 --stop-timeout 3600 sonarqube:community
``

Result:       
``
PS C:\WINDOWS\system32> docker run -d --name sonarqube -p 9000:9000 --stop-timeout 3600 sonarqube:community
1f7414e1d0913cfa6bb7a0da627e6111f244e38dc7b1ce294d6e4853a9cadbda
``

#### Recommended. Another way to Run SonarQube's container with outside volumes mountings
Source: https://docs.docker.com/storage/volumes/
Follow these steps for your first installation:

Creating the following volumes helps prevent the loss of information when updating to a new version or upgrading to a higher edition:
sonarqube_data: contains data files, such as the embedded H2 database and Elasticsearch indexes
sonarqube_logs: contains SonarQube logs about access, web process, CE process, and Elasticsearch
sonarqube_extensions: will contain any plugins you install and the Oracle JDBC driver if necessary.

##### Create the following volumes:                            
###### sonarqube_data volume
``
docker volume create --name sonarqube_data
``

Result:         
``
PS C:\WINDOWS\system32> docker volume create --name sonarqube_data
sonarqube_data
``
###### sonarqube_logs volume
``
docker volume create --name sonarqube_logs
``

Result:             
``
PS C:\WINDOWS\system32> docker volume create --name sonarqube_logs
sonarqube_logs
``
###### sonarqube_extensions volume
``
docker volume create --name sonarqube_extensions
``

Result:         
``
PS C:\WINDOWS\system32> docker volume create --name sonarqube_extensions
sonarqube_extensions
``

##### Check volumes list
``
docker volume ls
``

Result:             
``
PS C:\Users\iurii> docker volume ls
DRIVER    VOLUME NAME
local     d4a26e11c031121feb7a9147741c51ca44c45d737eabd38c8882eacd13c7fd30
local     sonarqube_data
local     sonarqube_extensions
local     sonarqube_logs
``

##### a. Start the SonarQube container with the embedded H2 database:
###### 1. no logs
``
docker run -d --name sonarqube --rm -p 9000:9000 -v sonarqube_extensions:/opt/sonarqube/extensions sonarqube:community
``

Result(no logs):         
``
PS C:\Users\iurii> docker run --rm -p 9000:9000 -v sonarqube_extensions:/opt/sonarqube/extensions sonarqube:community
...
see logs in devops/ci-cd/gitlab/logs/sonarqube-docker-run.log
``

###### 2. with logs
``
docker run -d --name sonarqube --rm -p 9000:9000 -v sonarqube_extensions:/opt/sonarqube/extensions sonarqube:community
``

Result(no logs):         
``
PS C:\Users\iurii> docker run -d --name sonarqube --rm -p 9000:9000 -v sonarqube_extensions:/opt/sonarqube/extensions sonarqube:community
497d41f15bfd7605bd99420d615b0abe7f51a2a851c3de4610cd623c0aaf514f
``

#### Check that SonarQube container is working
In a browser.
``
http://localhost:9000/
``

Result: SonarQube is working. Page "Log in to SonarQube" is opened.

#### Log in page
Use: admin/admin
#### Then on "Update your password" page
Change the password to "sonarqube".
