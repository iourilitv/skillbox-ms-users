# Creating pipeline on GitLab. Part 1-2
Trying to fix the problem in gitlab-ci-cd_2.md:     
"CommandNotFoundException": ObjectNotFound: (mvn:String).
Solved!

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
