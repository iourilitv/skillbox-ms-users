# Creating pipeline on GitLab. Part 1-0
Unsuccessfully!

Source: https://about.gitlab.com/blog/2016/12/14/continuous-delivery-of-a-spring-boot-application-with-gitlab-ci-and-kubernetes/

## Create "maven-build" stage
In .gitlab-ci.yml

Result.     
``
Pipeline failing? To keep GitLab spam and abuse free we ask that you verify your identity.
Until then, shared runners will be unavailable. Validate your account or use your own runners.
``

## Create my own runners
Sources:         
Install GitLab Runner on Windows: https://docs.gitlab.com/runner/install/windows.html       
Additionally Source:        
Install GitLab Runner on Docker and Ubuntu : https://www.youtube.com/watch?v=R58OuSts948&t=477s        

### Create a folder D:\Software\gitlab\GitLab-Runner.
### Download the binary for 64-bit from https://gitlab-runner-downloads.s3.amazonaws.com/latest/binaries/gitlab-runner-windows-amd64.exe
### Rename gitlab-runner-windows-amd64.exe to gitlab-runner.exe
### Run Windows PowerShell with Administrative privileges
Source: https://learn.microsoft.com/en-us/powershell/scripting/windows-powershell/starting-windows-powershell?view=powershell-7.3&viewFallbackFrom=powershell-7#with-administrative-privileges-run-as-administrator

Result:         
``
PS C:\WINDOWS\system32>
``

### Go to D:\Software\gitlab\GitLab-Runner
``
cd D:\Software\gitlab\GitLab-Runner
``

Result:         
``
PS D:\Software\gitlab\GitLab-Runner>
``

### Register a runner (in PowerShell)
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
gitlab,gitlab-docker-windows
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
Enter an executor: parallels, shell, ssh, virtualbox, custom, docker-windows, docker+machine, docker-ssh+machine, instance, kubernetes, docker, docker-ssh:
``

Step 7.
Choose and enter        
``
docker-windows
``

Result:
``
Enter the default Docker image (for example, mcr.microsoft.com/windows/servercore:1809):
``

Step 8.
Choose image for our project and enter        
``
maven:3-openjdk-11-slim:latest
``

Result:     
``
Runner registered successfully. Feel free to start it, but if it's running already the config should be automatically reloaded!
Configuration (with the authentication token) was saved in "D:\\Software\\gitlab\\GitLab-Runner\\config.toml"
PS D:\Software\gitlab\GitLab-Runner>
``

The runner has appeared in Assigned project runners chapter.      
But there is the following warning on the #20781651 (ibHVYnQR) instance: Runner has never contacted this instance.      
Just continue...

### Install GitLab Runner as a service (in PowerShell)
``
.\gitlab-runner.exe install
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe install
Runtime platform                                   arch=amd64 os=windows pid=2716 revision=12335144 version=15.8.0
``

### Run service using Built-in System Account(Recommended)
``
.\gitlab-runner.exe start
``

Result:     
``
PS D:\Software\gitlab\GitLab-Runner> .\gitlab-runner.exe start
Runtime platform                                    arch=amd64 os=windows pid=10868 revision=12335144 version=15.8.0
``

So, the problem has been solved.         
Now, there is a green circle across the #20781651 (ibHVYnQR) instance with the following text:     
Runner is online; last contact was 2 minutes ago


### Set up auto executable property by editing the runner 
Run untagged jobs : Indicates whether this runner can pick jobs without tags

## Error when job runs
``
Running with gitlab-runner 15.8.0 (12335144)
on gitlab-win64-runner ibHVYnQR, system ID: s_a677ecd18170
Preparing the "docker-windows" executor
00:00
ERROR: Failed to remove network for build
ERROR: Job failed: executor requires OSType=windows, but Docker Engine supports only OSType=linux
``

## See copies of runner's configuration at:
devops/ci-cd/gitlab/runners/configs/1       
!!!The example above was updated with changing executor to "instance".