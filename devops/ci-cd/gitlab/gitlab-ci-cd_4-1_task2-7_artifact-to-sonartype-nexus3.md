# Creating pipeline on GitLab. Part 4-1. "deploy" stage, place app's artifact into repository manager SonarType Nexus3
Installing and Running for the first time a docker container with SonarType Nexus OSS 3 server by Docker image.                      
Sources:            
Image Sonatype Nexus3 Docker: sonatype/nexus3: https://hub.docker.com/r/sonatype/nexus3/            
Менеджер репозиториев Sonatype Nexus 3: https://www.youtube.com/watch?v=6WjwrZknYVk
Successfully.

## Prepare environment.
Use devops/ci-cd/gitlab/gitlab-prepare-environment.md procedure.

## Installing and Running docker container with SonarType Nexus OSS 3 server by Docker image
Sources:         
Image Sonatype Nexus3 Docker: sonatype/nexus3: https://hub.docker.com/r/sonatype/nexus3/

### Pull the image into a local docker manually using docker engine (not necessary)
Using sonatype/nexus3 with "latest" tag.          
``
docker pull sonatype/nexus3
``      
Result:        
``
PS D:\projects\skillbox\microservices\users> docker pull sonatype/nexus3
Using default tag: latest
latest: Pulling from sonatype/nexus3
2562ea2dcfe3: Pull complete
...
2b94853eb97b: Pull complete
Digest: sha256:c7fdba87d4003871b06e2fa0c4e9cc2ba39a7d154eeb90998f97ee94fa3dddb7
Status: Downloaded newer image for sonatype/nexus3:latest
docker.io/sonatype/nexus3:latest
``

### Run sonatype/nexus3 docker container
#### Unrecommended. Easy way to Run without volumes
The image is pulling automatically if not exist in local docker.            
There:          
-d - the container starts as a daemon so the PS will be unlocked after the process done
-p {container's port}:{app's port}
--name {app name inside container}             
``
docker run -d -p 8081:8081 --name nexus sonatype/nexus3
``              
Result:       
``
PS D:\projects\skillbox\microservices\users> docker run -d -p 8081:8081 --name nexus sonatype/nexus3
bc5f08a6845c638055d27dad37e48035993570379eddf17935bdbb595ac3aab2
``

#### Recommended. Another way to Run sonatype/nexus3 container with inner docker volumes mountings
Persistent Data.                
There are two general approaches to handling persistent storage requirements with Docker:
1. Use a docker volume
2. Mount a host directory as the volume.
See Managing Data in Containers for additional information.
Use the 1-st approach.
##### Create the nexus-data volume                            
``
docker volume create --name nexus-data
``          
Result:                  
``
PS D:\projects\skillbox\microservices\users> docker volume create --name nexus-data
nexus-data
``
##### Check volumes list
``
docker volume ls
``          
Result:                     
``
PS D:\projects\skillbox\microservices\users> docker volume ls
DRIVER    VOLUME NAME
...
local     nexus-data
...
``
##### Run nexus's docker container with docker volume
``
docker run -d -p 8081:8081 --name nexus -v nexus-data:/nexus-data sonatype/nexus3
``          
Result:         
``
PS D:\projects\skillbox\microservices\users> docker run -d -p 8081:8081 --name nexus -v nexus-data:/nexus-data sonatype/nexus3
f863d7d5ecdee0cb8255ed394734feb2a74db3e057648254af0ec8a398525b02
``

#### Stop sonatype/nexus3 docker container
When stopping, be sure to allow sufficient time for the databases to fully shut down.                  
docker stop --time={secs} <CONTAINER_NAME>
There:      
--time {time(sec) to keep the container alive - docker could stop after 10 secs}
``
docker stop --time=120 nexus
``              
Result:         
``
PS D:\projects\skillbox\microservices\users> docker stop --time=120 nexus
nexus
``

#### Check that nexus container is working
!!! Do this in at least 10 minutes after the nexus container started  
``
curl -Uri "http://localhost:8081/"
``
or          
call the following url in a browser          
``
http://localhost:8081/
``          
Result:                       
Sonartype Nexus Repository Manager OSS is working. Page "Welcome" is opened.

## Work with Sonartype Nexus Repository Manager OSS
In browser.             
### Sign in
Click Sign in and see:          
"Your admin user password is located in
/nexus-data/admin.password on the server."
#### Get default password
In DockerDesktop open nexus container. In the Terminal tab
``
cat /nexus-data/admin.password
``          
Result:             
``
sh-4.4$ cat /nexus-data/admin.password
60fdcfd6-5b9f-41b3-af3a-e88796f54134sh-4.4$
``              
Use the following data to sign in:        
Username: admin         
Password: 60fdcfd6-5b9f-41b3-af3a-e88796f54134

### Then on "Update your password" page
#### Set new password
Input a new password: nexus.
#### Set access mode to repository
Configure Anonymous Access3 of 4
Enable anonymous access means that by default, users can search, browse and download components from repositories without credentials. Please consider the security implications for your organization.         
Disable anonymous access should be chosen with care, as it will require credentials for all users and/or build tools.               
Choose: Enable anonymous access.      

Successfully!
Upload item appears in the left menu. 