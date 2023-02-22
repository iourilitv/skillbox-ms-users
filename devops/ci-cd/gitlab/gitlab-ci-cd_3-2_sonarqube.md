# Creating pipeline on GitLab. Part 3-2. "sonarqube-check" stage (sonarqube for Pull Requests)
Continue "Part 3-1" configuring the project for code analysing by SonarQube.           
Successfully.
It works even after restart Windows System on the PC.

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

## Not Recommended. Start easily the code analyzing by SonarQube (deprecated properties)
Source:             
How to integrate your MAVEN project with SonarQube: https://medium.com/knoldus/how-to-integrate-your-maven-project-with-sonarqube-79f7368f8c7a

In PS D:\projects\skillbox\microservices\users>             
``
mvn clean install;
mvn sonar:sonar
``              
or            
``
mvn clean install sonar:sonar
``          
or (put properties into command line)
!!!Attention. In PS add ` before every -D

``
mvn clean install sonar:sonar `-Dsonar.host.url=http://localhost:9000 `-Dsonar.login=admin `-Dsonar.password=sonarqube
``

Result:             
ANALYSIS SUCCESSFUL, you can find the results at: http://localhost:9000/dashboard?id=com.example.microservices%3Ausers 
See logs in devops/ci-cd/gitlab/logs/sonar-local-start.log.             
Coverage is 0%.              

## Recommended. Start the code analyzing by SonarQube
Another way to Start the code analyzing by SonarQube                
Source:                               
Code Analysis with SonarQube: https://www.baeldung.com/sonar-qube

### Update some sonar.* properties in pom.xml:
#### Move into command line the following properties:       
- sonar.host.url;
- sonar.login.
#### Remove the property:
- sonar.password.

### Generate user token in SonarQube User / My Account / Security
In browser in SonarQube interface in Authorized mode (admin).              
In the top-right corner click on your profile's icon and choose My Account.                
In top-right menu click on Security.
#### Generate a new token in "Generate Tokens" section
Write down or choose:           
Name: user-admin-no-exp;    
Type: User Token;               
Expires in: No expiration.               
Then press "Generate" button
#### Save generated token to any place in your system
squ_1af8d87893a5ce56f3b5975216a103a804e97b27

### Run SonarQube code analyzing with authorization by user token 
In PS D:\projects\skillbox\microservices\users>     
``
mvn clean install sonar:sonar `-Dsonar.host.url=http://localhost:9000 `-Dsonar.login=squ_31c99d8483c6c42c155df4c665cc5614a6cead71
``

Result:         
``
...
SUCCESSFUL, you can find the results at: http://localhost:9000/dashboard?id=com.example.microservices%3Ausers
...
``
See logs in devops/ci-cd/gitlab/logs/sonarqube-docker-run-user-token.log
(with warnings because there is "login" property in pom.xml)