# kubernetes (K8s). Part 2
Creating deployment for users-app

## Create deployment and service by yaml-file 
### Create deployment and service for the app users-app by users-app-deployment.yaml
``
kubectl apply -f users-app-deployment.yaml
``

result:

``
deployment.apps/users-app-deployment created
service/users-app-service created
``

### Check pods list

``
kubectl get pods
``

result:

``
PS D:\projects\examples\microservices\users> kubectl get pods
NAME                                     READY   STATUS    RESTARTS   AGE
users-app-deployment-577c6f88fb-9q79r    1/1     Running   0          3m41s
users-app-deployment-577c6f88fb-cf45x    1/1     Running   0          3m40s
users-pg-db-deployment-bdc84b778-kj6dp   1/1     Running   0          155m
users-pg-db-deployment-bdc84b778-tqbvd   1/1     Running   0          155m
``

### Check if the service opens in a browser 

``
minikube service users-app-service
``

result:

``
PS D:\projects\examples\microservices\users> minikube service users-app-service
|-----------|-------------------|-------------|-----------------------------|
| NAMESPACE |       NAME        | TARGET PORT |             URL             |
|-----------|-------------------|-------------|-----------------------------|
| default   | users-app-service |        8001 | http://192.168.59.102:30001 |
|-----------|-------------------|-------------|-----------------------------|
* Opening service default/users-app-service in default browser...
``

### Check if the tables have been created
In pgAdmin:  192.168.59.102:30543 | db: users | username: microuser | password: microuser

The tables have been created correctly.

### Check if OpenApi is acceptable 
In browser: http://192.168.59.102:30001/swagger-ui/index.html

Swagger works correctly

### Check if the app is acceptable for requests
In Postman: http://192.168.59.102:30001/users

Swagger works correctly
