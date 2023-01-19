# kubernetes (K8s). Part 2
Creating deployment for users-pg-db

## Minikube restarting
### For restart the exists minikube created in the Part 1
``
minikube start --driver=virtualbox --no-vtx-check
``

## Create deployment and service by yaml-file 
### Create deployment and service for the app users-pg-db by users-pg-db-deployment.yaml
``
kubectl apply -f users-pg-db-deployment.yaml
``

result:

``
deployment.apps/users-pg-db-deployment created
service/users-pg-db-service created
``

But it is with ERROR because environments of container are not configured

``
PS D:\projects\examples\microservices\users> kubectl get deployment.apps
NAME                     READY   UP-TO-DATE   AVAILABLE   AGE
users-pg-db-deployment   0/2     2            0           9s
PS D:\projects\examples\microservices\users> kubectl get pods
NAME                                      READY   STATUS   RESTARTS   AGE
users-pg-db-deployment-6b6dfff87d-68p54   0/1     Error    0          20s
users-pg-db-deployment-6b6dfff87d-lcpdx   0/1     Error    0          20s
``

### Delete deployment and service for the app users-pg-db by users-pg-db-deployment.yaml
``
kubectl delete -f users-pg-db-deployment.yaml
``

result:

``
deployment.apps "users-pg-db-deployment" deleted
service "users-pg-db-service" deleted
``

### Tried to add volumes into the deployment like it was done in docker-compose.yml but not successfully
Used users-pg-db-deployment-volumes.yaml file for deployment
See devops/K8s/logs/issue-deployment-with-volumes.log

So trying to do this manually.

### Add env block into the file and repeat the creating
``
PS D:\projects\examples\microservices\users> kubectl get pods
NAME                                     READY   STATUS    RESTARTS   AGE
users-pg-db-deployment-bdc84b778-kj6dp   1/1     Running   0          16m
users-pg-db-deployment-bdc84b778-tqbvd   1/1     Running   0          16m
``

### Opening users-pg-db-service default/users-pg-db-service in browser
``
PS D:\projects\examples\microservices\users> minikube service users-pg-db-service
|-----------|---------------------|-------------|-----------------------------|
| NAMESPACE |        NAME         | TARGET PORT |             URL             |
|-----------|---------------------|-------------|-----------------------------|
| default   | users-pg-db-service |       54321 | http://192.168.59.102:30543 |
|-----------|---------------------|-------------|-----------------------------|
* Opening service default/users-pg-db-service in default browser...
``

### Create users_scheme schema by pgAdmin manually
``
CREATE SCHEMA users_scheme
AUTHORIZATION microuser;
COMMENT ON SCHEMA users_scheme
IS 'The main schema of microcervice "users" for users-pg-db-service in k8s minikube';
``
