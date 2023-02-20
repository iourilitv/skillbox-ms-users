# Creating pipeline on GitLab. Part 3-2. "sonarqube-check" stage (sonarqube for Pull Requests)
Continue "Part 3-2" configuring CI/CD settings on gitlab for the project's code analysing by SonarQube.           


## Environment.
Continue with gitlab-ci-cd_3-2_sonarqube.md state.

## Configure CI/CD settings on gitlab
In project skillbox-ms-users / Settings / CI/CD / Variables.
### Add variables for SonarQube
Settings for all variables(Type: Variable; Options: Protected, Expanded; Environments: All (default)).           
Other settings({Key}: {Value}):         
- SONAR_HOST_URL: http://localhost:9000;
- SONAR_LOGIN_TOKEN: squ_1af8d87893a5ce56f3b5975216a103a804e97b27.

## Configure the project's CI/CD settings for SonarQube usage
### Add sonarqube-check job at the build stage into .gitlab-ci.yml
Trying to run without the project's variables.