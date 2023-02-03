# Creating pipeline on GitLab. Part 1-2. "maven-build" stage
Trying to fix the problem in gitlab-ci-cd_1-1.md:     
"CommandNotFoundException": ObjectNotFound: (mvn:String).
Solved!
But there is another problem: "JUnit tests falls"

## The problem's reason is that the path to maven has been set only for the User
### Add the path to maven for the System user as well 
Windows Start/Settings/System           
then /About on the left menu/Additional system settings on the right menu           
then press "Environment variables" and work with System variables part         
then edit "Path" variable - create "C:\apache-maven-3.6.3\bin" 

## Run PowerShell as administrator 
### Go to D:\Software\gitlab\GitLab-Runner

### Stop the runner 
``
.\gitlab-runner.exe stop
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe stop
Runtime platform                                    arch=amd64 os=windows pid=3428 revision=12335144 version=15.8.0
``

### Start runner again
``
./gitlab-runner.exe start
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe start
Runtime platform                                    arch=amd64 os=windows pid=2528 revision=12335144 version=15.8.0
``

Result: This problem is solved.         

## There is another problem "JUnit tests falls"
### The reason:
"SpringApplication: Application run failed because of nested exception is java.io.FileNotFoundException:        
class path resource [private.properties] cannot be opened because it does not exist"

### Logs
``
..
2023-02-03 13:52:09.350  WARN 3088 --- [           main] o.s.w.c.s.GenericWebApplicationContext   : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [com.example.microservices.users.UsersApplication]; nested exception is java.io.FileNotFoundException: class path resource [private.properties] cannot be opened because it does not exist
2023-02-03 13:52:09.490 ERROR 3088 --- [           main] o.s.boot.SpringApplication               : Application run failed
org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [com.example.microservices.users.UsersApplication]; nested exception is java.io.FileNotFoundException: class path resource [private.properties] cannot be opened because it does not exist
..
``
