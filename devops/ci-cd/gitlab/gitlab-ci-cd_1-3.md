# Creating pipeline on GitLab. Part 1-3
Trying to fix the problem in gitlab-ci-cd_1-2.md:     
"JUnit tests falls"     

## The reason:
"SpringApplication: Application run failed because of nested exception is java.io.FileNotFoundException:        
class path resource [private.properties] cannot be opened because it does not exist"

## Solving the problem
### Delete the following files:
resources/private.properties;      
config/AppConfig.java
