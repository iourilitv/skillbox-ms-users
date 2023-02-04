# Creating pipeline on GitLab. Part 2. "maven-integration-tests" stage (itests)
Source: https://about.gitlab.com/blog/2016/12/14/continuous-delivery-of-a-spring-boot-application-with-gitlab-ci-and-kubernetes/

## Prepare environment. Do operations if still have not done
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

## Create "maven-integration-tests" stage
### Add "maven-integration-tests" stage in .gitlab-ci.yml

### Commit and push changes to gitlab for checking CI/CD 

Result.     
``
Waiting a result
``

