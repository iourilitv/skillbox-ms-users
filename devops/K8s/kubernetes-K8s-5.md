# kubernetes (K8s). Part 5
Replacing the services of kind LoadBalancer with a single ingress controller.

Additional source: https://www.youtube.com/watch?v=ThP-OEjpDZk

## Install ingress controller: nginx
!!! How to solve a problem find in devops/K8s/issue-ingress-installing.md       
``
minikube addons enable ingress
``

Result:     
``
...
* The 'ingress' addon is enabled
``

## Configure ingress
### Create users-ingress.yaml

### Create ingress
``
kubectl apply -f users-ingress.yaml
``

Result:
``
ingress.networking.k8s.io/users-ingress created
``

## Configure DNS server in order to every request forwards to the ingress controller
### Get address of ingress
``
kubectl get ingress
``

Result:     
``
PS D:\projects\examples\microservices\users> kubectl get ingress
NAME            CLASS   HOSTS         ADDRESS          PORTS   AGE
users-ingress   nginx   users.local   192.168.59.102   80      3m
``

### Set address if it had not set automatically
``
minikube service users-ingress
``
Ignore a result.

### Added ingress address into /etc/hosts file
Open hosts in C:\Windows\System32\drivers\etc.      
Add the following row:  
192.168.59.102 users.local

### Check in browser
``
http://users.local/swagger-ui/index.html
``

### Check by Postman
Do Get http://users.local/users instead of http://192.168.59.102:30001/users

### Update the service behind ingress
``
PS D:\projects\examples\microservices\users> kubectl apply -f users-app-deployment.yaml
deployment.apps/users-app-deployment unchanged
service/users-app-service configured
``

### Check services
users-app-service has Type: ClusterIP now
``
PS D:\projects\examples\microservices\users> kubectl get services
NAME                  TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)           AGE
kubernetes            ClusterIP      10.96.0.1       <none>        443/TCP           2d3h
users-app-service     ClusterIP      10.103.91.123   <none>        8001/TCP          23h
users-pg-db-service   LoadBalancer   10.96.188.232   <pending>     54321:30543/TCP   26h
``

### Stop minikube
``
PS D:\projects\examples\microservices\users> minikube stop
* Узел "minikube" останавливается ...
* Остановлено узлов: 1.
``