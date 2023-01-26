# kubernetes (K8s). Part 4
Moving env variables from users-app-deployment.yaml file to users-config.yaml.

## Create users-config.yaml file 
Copy env variables from users-app-deployment.yaml into users-config.yaml

### Apply settings users-config.yaml
``
kubectl apply -f users-config.yaml
`` 

result:     
``
configmap/users-config created
``

### Replace env variables in users-app-deployment.yaml with values from users-config.yaml
Instead of this:    
value: "users-pg-db-service"

use the following:  
valueFrom:  
configMapKeyRef:    
name: users-config  
key: postgres_db_host

### Update the app deployment
Repeat operations from devops/K8s/kubernetes-K8s-3.md

Result:     
``
PS D:\projects\examples\microservices\users> kubectl apply -f users-app-deployment.yaml
deployment.apps/users-app-deployment configured
service/users-app-service unchanged
``