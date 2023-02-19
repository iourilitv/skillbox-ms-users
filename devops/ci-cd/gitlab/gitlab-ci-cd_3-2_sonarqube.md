# Creating pipeline on GitLab. Part 3-2. "sonarqube-check" stage (sonarqube for Pull Requests)
Configuring the project for code analysing by SonarQube.           
Sources:        

## Environment.
Continue with gitlab-ci-cd_3-1_sonarqube.md state.

## Configure the project settings for SonarQube usage
### Add into pom.xml the following:
#### build plugins
- sonar-maven-plugin;
- jacoco-maven-plugin (for result reports)
#### properties (deprecated, unrecommended)
- <sonar.host.url>http://localhost:9000</sonar.host.url>
- <sonar.login>admin</sonar.login>
- <sonar.password>sonarqube</sonar.password>
- <sonar.coverage.exclusions>**/*Application.*</sonar.coverage.exclusions>

## Start the code analyzing by SonarQube
In PS D:\projects\skillbox\microservices\users>             
``
mvn clean install;
mvn sonar:sonar
``              
or            
``
mvn clean install sonar:sonar
``

Result:             
ANALYSIS SUCCESSFUL, you can find the results at: http://localhost:9000/dashboard?id=com.example.microservices%3Ausers 
See logs in devops/ci-cd/gitlab/logs/sonar-local-start.log.             
Coverage is 0%.              

