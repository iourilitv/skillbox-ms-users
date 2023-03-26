# Creating pipeline on GitLab. Part 1-1. "maven-build" stage
Trying to fix the problem in gitlab-ci-cd_1-0.md.
Solved but there is another one "CommandNotFoundException": ObjectNotFound: (mvn:String).

## Delete exist runner
### Remove exist runner on gitlab account
On gitLab come into the project https://gitlab.com/iourilitv/skillbox-ms-users
then go to the left menu and click Settings/CI/CD then push Expand on Runners chapter.
Then come to Project runners section.

``
Push "Remove runner" button on the runner.
``

Go to D:\Software\gitlab\GitLab-Runner

### Stop the runner 
``
.\gitlab-runner.exe stop
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe stop
Runtime platform                                    arch=amd64 os=windows pid=3428 revision=12335144 version=15.8.0
``

### Uninstall the runner
``
.\gitlab-runner.exe uninstall
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe uninstall
Runtime platform                                    arch=amd64 os=windows pid=6480 revision=12335144 version=15.8.0
``

### Delete config files in D:\Software\gitlab\GitLab-Runner:
.runner_system_id and config.toml

## Create my own runners from the beginning
Source:         
How to install, register and start GitLab Runner on Windows: https://techdirectarchive.com/2021/09/28/how-to-install-register-and-start-gitlab-runner-on-windows/

Just continue...

### Install GitLab Runner as a service (in PowerShell)
``
./gitlab-runner.exe install
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe install
Runtime platform                                    arch=amd64 os=windows pid=18716 revision=12335144 version=15.8.0
``

### Run service using Built-in System Account(Recommended)
``
./gitlab-runner.exe start
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe start
Runtime platform                                    arch=amd64 os=windows pid=2528 revision=12335144 version=15.8.0
``

### Check a version
``
./gitlab-runner.exe --version
``

Result:         
``
PS D:\Software\gitlab\GitLab-Runner> ./gitlab-runner.exe --version
Version:      15.8.0
Git revision: 12335144
Git branch:   15-8-stable
GO version:   go1.18.9
Built:        2023-01-19T03:19:01+0000
OS/Arch:      windows/amd64
``

### Register a Project's runner (in PowerShell)
#### Run the following command:
``
.\gitlab-runner.exe register
``

Result (do the following steps and enter required information on some steps):     
On gitLab come into the project https://gitlab.com/iourilitv/skillbox-ms-users
then go to the left menu and click Settings/CI/CD then push Expand on Runners chapter.
Then come to Project runners section.
Step 1.     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe register
Runtime platform                                    arch=amd64 os=windows pid=14296 revision=12335144 version=15.8.0
Created missing unique system ID                    system_id=s_a677ecd18170
Enter the GitLab instance URL (for example, https://gitlab.com/):
``

Step 2.           
Copy from 2. Register the runner with this URL:https://gitlab.com/ 
and enter        
``
https://gitlab.com/
``

Result:     
``
Enter the registration token:
``

Step 3.       
Copy from 2. .. And this registration token:GR1348941Y9VzVpjyv-yPFHNKJfGE
and enter           
``
GR1348941Y9VzVpjyv-yPFHNKJfGE
``

Result:     
``
Enter a description for the runner:
``

Step 4.
Imagine and enter gitlab-win64-local      
``
[IOURI-X555L]: gitlab-win64-local
``

Result:     
``
Enter tags for the runner (comma-separated):
``

Step 5.
Imagine and enter       
``
gitlab,gitlab-shell-windows
``

Result:     
``
Enter optional maintenance note for the runner:
``

Step 6.
Imagine and enter       
``
Local runner on Windows10 Home 64-bit
``

Result:     
``
WARNING: Support for registration tokens and runner parameters in the 'register' command has been deprecated in GitLab Runner 15.6 and will be replaced with support for authentication tokens. For more information, see https://gitlab.com/gitlab-org/gitlab/-/issues/380872
Registering runner... succeeded                     runner=GR1348941Y9VzVpjy
Enter an executor: docker-windows, docker+machine, instance, kubernetes, custom, docker-ssh, parallels, shell, ssh, virtualbox, docker-ssh+machine, docker:
``

Step 7.
Choose and enter        
``
shell
``

Result:
``
Runner registered successfully. Feel free to start it, but if it's running already the config should be automatically reloaded!
Configuration (with the authentication token) was saved in "D:\\Software\\gitlab\\GitLab-Runner\\config.toml"
``

The runner has appeared in "Assigned project runners" chapter.       
There is a green circle across the #20791452 (yxuxCRtR) instance with the following text:     
Runner is online; last contact was 2 minutes ago

### Set up auto executable property by editing the runner 
Run untagged jobs : Indicates whether this runner can pick jobs without tags

## Error "Preparing the "shell" executor" when job runs
``
Running with gitlab-runner 15.8.0 (12335144)
on gitlab-win64-local yxuxCRtR, system ID: s_a677ecd18170
Preparing the "shell" executor
00:00
Using Shell executor...
Preparing environment
00:00
ERROR: Job failed (system failure): prepare environment: failed to start process: exec: "pwsh": executable file not found in %PATH%. Check https://docs.gitlab.com/runner/shells/index.html#shell-profile-loading for more information
``

## Solve the "Preparing the "shell" executor" error
Source:             
exec: "pwsh": executable file not found in %PATH% : https://stackoverflow.com/questions/68109273/exec-pwsh-executable-file-not-found-in-path
### Update the config.toml file
``
Replace shell = "pwsh" with "powershell"
``

Result: This problem is solved.         

### But the there is another one: 
"CommandNotFoundException"

#### Logs:       
Running with gitlab-runner 15.8.0 (12335144)
on gitlab-win64-local yxuxCRtR, system ID: s_a677ecd18170
Preparing the "shell" executor
00:00
Using Shell executor...
Preparing environment
Running on IOURI-X555L...
Getting source from Git repository
Fetching changes with git depth set to 20...
Initialized empty Git repository in D:/Software/gitlab/GitLab-Runner/builds/yxuxCRtR/0/iourilitv/skillbox-ms-users/.git/
Created fresh repository.
Checking out 1739aa0a as ci-cd...
git-lfs/2.7.1 (GitHub; windows amd64; go 1.11.5; git 6b7fb6e3)
Skipping object checkout, Git LFS is not installed.
Skipping Git submodules setup
Executing "step_script" stage of the job script
$ mvn package -B
mvn : ��� "mvn" �� ��ᯮ����� ��� ��� ����������, �㭪樨, 䠩�� �業���� ��� �믮��塞�� �ணࠬ��. �஢����� �ࠢ����
���� ����ᠭ�� �����, � ⠪�� ����稥 � �ࠢ��쭮��� ����, ��᫥ 祣� ��������� �������.
��ப�:255 ����:1
+ mvn package -B
+ ~~~
    + CategoryInfo          : ObjectNotFound: (mvn:String) [], CommandNotFoundException
    + FullyQualifiedErrorId : CommandNotFoundException

Cleaning up project directory and file based variables
00:01
ERROR: Job failed: exit status 1

### Register a Group's runner (in PowerShell)
Group: study-projects-iourilitv/skillbox/microservices (Created 25.03.23)       
Do the same as in chapter ### Register a Project's runner (in PowerShell).          
#### Register gitlab-study-projects-iourilitv-win64-local
!!!!! Using the same runner's agent as for the project runner, gitlab-win64-local.

### Set up auto executable property by editing the runner
Run untagged jobs : Indicates whether this runner can pick jobs without tags

#### Logs
``
PS D:\Software\gitlab\GitLab-Runner> ./gitlab-runner.exe register
Runtime platform                                    arch=amd64 os=windows pid=10352 revision=12335144 version=15.8.0
Enter the GitLab instance URL (for example, https://gitlab.com/):
https://gitlab.com/
Enter the registration token:
GR1348941szQQz5Eq26m-j4Y5tB7d
Enter a description for the runner:
[IOURI-X555L]: gitlab-study-projects-iourilitv-win64-local
Enter tags for the runner (comma-separated):
gitlab,gitlab-shell-windows
Enter optional maintenance note for the runner:
Local group runner on Windows10 Home 64-bit
WARNING: Support for registration tokens and runner parameters in the 'register' command has been deprecated in GitLab Runner 15.6 and will be replaced with support for authentication tokens. For more information, see https://gitlab.com/gitlab-org/gitlab/-/issues/380872
Registering runner... succeeded                     runner=GR1348941szQQz5Eq
Enter an executor: docker-ssh, instance, kubernetes, custom, docker-windows, shell, ssh, virtualbox, docker+machine, docker-ssh+machine, docker, parallels:
shell
Runner registered successfully. Feel free to start it, but if it's running already the config should be automatically reloaded!
Configuration (with the authentication token) was saved in "D:\\Software\\gitlab\\GitLab-Runner\\config.toml"
``
