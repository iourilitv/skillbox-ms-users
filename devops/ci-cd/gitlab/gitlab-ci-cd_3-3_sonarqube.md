# Creating pipeline on GitLab. Part 3-2. "sonarqube-check" stage (sonarqube for Pull Requests)
Continue "Part 3-2" configuring CI/CD settings on gitlab for the project's code analysing by SonarQube.           


## Environment.
Continue with gitlab-ci-cd_3-2_sonarqube.md state.

## Configure CI/CD settings on gitlab
In project skillbox-ms-users / Settings / CI/CD / Variables.
### Add variables for SonarQube
Settings for all variables(Type: Variable; Options: Expanded; Environments: All (default)).           
Other settings({Key}: {Value}):         
- SONAR_HOST_URL: http://localhost:9000;
- SONAR_LOGIN_TOKEN: squ_1af8d87893a5ce56f3b5975216a103a804e97b27.

## Configure the project's CI/CD settings for SonarQube usage
### Add sonarqube-check job at the build stage into .gitlab-ci.yml

## Trying to run pipeline
### Doesn't work. Without the project's variables
Unsuccessfully for the Merge Request.  

Result: 
``
...
[ERROR] Failed to execute goal org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar (default-cli) on project users: Not authorized. Analyzing this project requires authentication. Please provide a user token in sonar.login or other credentials in sonar.login and sonar.password.
...
``

### Add the project's variables into sonarqube-check:script: in .gitlab-ci.yml
Result: The same ERROR.     
#### Reason: The project's variables have Options: Protected.
#### Solution
Remove "Protected" from the Options. Options: Expanded is correct.

Merge Request's pipeline with only one job is passing now successfully. 