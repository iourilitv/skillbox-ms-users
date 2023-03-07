# Creating pipeline on GitLab. Part 4-2. "deploy" stage, place app's artifact into repository manager SonarType Nexus3
Working for the first time a docker container with SonarType Nexus OSS 3 server by Docker image.                      
Continue devops/ci-cd/gitlab/gitlab-ci-cd_4-1_task2-7_artifact-to-sonartype-nexus3.md.
Sources:            
Менеджер репозиториев Sonatype Nexus 3(since 16:30): https://www.youtube.com/watch?v=6WjwrZknYVk


## Prepare environment.
Use devops/ci-cd/gitlab/gitlab-prepare-environment.md procedure.

## Create repositories skillbox maven2 hosted: release and snapshot
In Nexus web client.                    
Left Menu: Repository/Repositories / "Create repository" button
### Create Maven2(hosted) repository: skillbox-mvn-hosted-release
Fill a form:
#### 
- Name: skillbox-mvn-hosted-mixed
- Online: {checked} If checked, the repository accepts incoming requests
#### Maven 2:
- Version policy(Release, Snapshot, Mixed): Release (What type of artifacts does this repository store?)
- Layout policy(Strict, Permissive): Strict (Validate that all paths are maven artifact or metadata paths)
- Content Disposition(Inline, Attachment): Inline (Add Content-Disposition header as 'Attachment' to disable some content from being inline in a browser.)
#### Storage:
- Blob store(default): default (Blob store used to store repository contents)
- Strict Content Type Validation: {checked} (Validate that all content uploaded to this repository is of a MIME type appropriate for the repository format)
#### Hosted
- Deployment policy(Allow redeploy, Disable redeploy, Read-only, Deploy by Replication Only): Disable redeploy (Controls if deployments of and updates to artifacts are allowed)
- Proprietary Components: {unchecked} (Components in this repository count as proprietary for namespace conflict attacks (requires Sonatype Nexus Firewall))
#### Cleanup
- Cleanup Policies: (Components that match any of the Applied policies will be deleted)
  - Available: {Empty}
  - Applied: {Empty}

### Create Maven2(hosted) repository: skillbox-mvn-hosted-snapshot
Follow the steps as for release repository with only one changing:      
Replace and use "snapshot" instead of "release".

### Create Maven2(hosted) group of repositories: skillbox-maven-group-release (Not sure that this is required)
Add maven's not snapshot repositories and skillbox-mvn-hosted-release

## Configure a deployment stage in gitlab pipeline
Sources:        
Sonartype Documentation. Nexus Repository Maven Plugin: https://help.sonatype.com/repomanager3/integrations/nexus-repository-maven-plugin       
How to Use Gitlab-CI with Nexus (Since Step 5: Configure the Nexus deployment): https://blog.sonatype.com/how-to-use-gitlab-ci-with-nexus       

### Configure .m2/settings.xml
#### Create if not exists settings.xml in C:\Users\{username}\.m2 directory
#### Fill up it with the following:
Add nexus servers as they named in distributionManagement/.../id into pom.xml     
``
<settings xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.1.0 http://maven.apache.org/xsd/settings-1.1.0.xsd"
xmlns="http://maven.apache.org/SETTINGS/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <servers>
    <server>
      <id>release</id>
      <username>${env.Nexus_REPO_USER}</username>
      <password>${env.Nexus_REPO_PASS}</password>
    </server>
    <server>
      <id>snapshot</id>
      <username>${env.Nexus_REPO_USER}</username>
      <password>${env.Nexus_REPO_PASS}</password>
    </server>
  </servers>
</settings>
<!-- Username and password will be replaced by the correct values using variables.-->
``

### Add distributionManagement into pom.xml
``
<distributionManagement>
  <repository>
    <id>release</id>
    <url>${env.Nexus_REPO_URL}release</url>
  </repository>
  <snapshotRepository>
    <id>snapshot</id>
    <url>${env.Nexus_REPO_URL}snapshot</url>
  </snapshotRepository>
</distributionManagement>
``

### Check the app deploying locally 
``
mvn deploy -DskipTests=true `-Denv.Nexus_REPO_URL=http://localhost:8081/repository/skillbox-mvn-hosted- `-Denv.Nexus_REPO_USER=admin `-Denv.Nexus_REPO_PASS=nexus
``      
Results:      
SNAPSHOT    
It's allowed to redeploy the same snapshot version even under "Disable redeploy" mode.      
``
...
[INFO] --- maven-deploy-plugin:2.8.2:deploy (default-deploy) @ users ---
Uploading to release: http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservices/users/0.0.1-RELEASE/users-0.0.1-RELEASE.jar
Uploaded to release: http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservices/users/0.0.1-RELEASE/users-0.0.1-RELEASE.jar (53 MB at 7.0 MB/s)
Uploading to release: http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservices/users/0.0.1-RELEASE/users-0.0.1-RELEASE.pom
Uploaded to release: http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservices/users/0.0.1-RELEASE/users-0.0.1-RELEASE.pom (11 kB at 29 kB/s)
Downloading from release: http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservices/users/maven-metadata.xml
Uploading to release: http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservices/users/maven-metadata.xml
Uploaded to release: http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservices/users/maven-metadata.xml (326 B at 268 B/s)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
``

RELEASE     
If under "Disable redeploy" mode to try to redeploy the same released version then the following error appears: 
``
...
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-deploy-plugin:2.8.2:deploy (default-deploy) on project users: Failed to deploy artifacts: Could not transfer artifact com.example.microservices:u
sers:jar:0.0.1-RELEASE from/to release (http://localhost:8081/repository/skillbox-mvn-hosted-release): Transfer failed for http://localhost:8081/repository/skillbox-mvn-hosted-release/com/example/microservic
es/users/0.0.1-RELEASE/users-0.0.1-RELEASE.jar 400 Repository does not allow updating assets: skillbox-mvn-hosted-release -> [Help 1]
...
``

### Update the pipeline on gitlab
#### Configure .gitlab-ci.yml
- Add a new stage: deploy
- Add a new job: 
````
deploy-to-nexus:
stage: deploy
  image: $MAVEN_IMAGE
  script:
    - mvn deploy -DskipTests=true `-Denv.Nexus_REPO_URL=${NEXUS_REPO_URL} `-Denv.Nexus_REPO_USER=${NEXUS_REPO_USER} `-Denv.Nexus_REPO_PASS=${NEXUS_REPO_PASS}
  when:
    manual
````

#### Create on gitlab the following project variables(Expanded)
- NEXUS_REPO_URL: http://localhost:8081/repository/skillbox-mvn-hosted-
- NEXUS_REPO_USER: admin
- NEXUS_REPO_PASS: nexus