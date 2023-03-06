# Prepare environment for CI/CD on gitlab. 
Do operations if they still have not done

## 1. Run Docker
### Install Docker Desktop for Window
Source: https://docs.docker.com/desktop/install/windows-install/
### Run docker-demon
Run Docker Desktop for Window for it

## 2. Run gitlab runner
### Run PowerShell (PS) as administrator
### In PS go to D:\Software\gitlab\GitLab-Runner
``
cd D:\Software\gitlab\GitLab-Runner
``      
Next working in PS D:\Software\gitlab\GitLab-Runner>            
### Start default gitlab runner with PS: gitlab-win64-local
``
./gitlab-runner.exe start
``      
Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe start
Runtime platform                                    arch=amd64 os=windows pid=3280 revision=12335144 version=15.8.0
``

## 3. Run in Docker DeskTop the following containers:
### sonarqube - CodeQuality checking server
### nexus - Sonartype Nexus Repository Manager OSS

## 4. Run 2-nd PS in the project directory
``
cd D:\projects\skillbox\microservices\users
``          
Next working in D:\projects\skillbox\microservices\users>           

