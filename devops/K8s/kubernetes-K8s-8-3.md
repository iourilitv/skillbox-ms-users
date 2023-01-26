# kubernetes (K8s). Part 8-3
Cleaning and repeating the deployments in Part 8-2 to resolve issue-minikube-not-save-state.md.     
Solved!

Sources: https://www.bmc.com/blogs/kubernetes-postgresql/;
https://kubernetes.io/docs/tutorials/stateful-application/basic-stateful-set/

## Clean up all
### Delete Pod, Services and other items by helm
``
helm delete users-helm
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm delete users-helm
release "users-helm" uninstalled
``

Control that all items in users-helm, excluding the volumes and claim, have been deleted.

### Delete the Claims in dashboard/Persistent Volume Claims
``
kubectl delete pvc users-pg-db-claim
kubectl delete pvc users-pg-db-volume-users-pg-db-deployment-0
kubectl delete pvc users-pg-db-volume-users-pg-db-deployment-1
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl delete pvc users-pg-db-claim
persistentvolumeclaim "users-pg-db-claim" deleted
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl delete pvc users-pg-db-volume-users-pg-db-deployment-0
persistentvolumeclaim "users-pg-db-volume-users-pg-db-deployment-0" deleted
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl delete pvc users-pg-db-volume-users-pg-db-deployment-1
persistentvolumeclaim "users-pg-db-volume-users-pg-db-deployment-1" deleted
``

Control that all items in dashboard/Persistent Volume Claims have been deleted.

### Delete the Volumes in dashboard/Persistent Volumes
``
kubectl delete pv users-pg-db-volume
kubectl delete pv pvc-4fdb7fa7-fb43-4657-963e-4c4487e0f9d5
kubectl delete pv pvc-32612509-361e-4f6a-885e-b91802e4c742
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl delete pv users-pg-db-volume
persistentvolume "users-pg-db-volume" deleted
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl delete pv pvc-4fdb7fa7-fb43-4657-963e-4c4487e0f9d5
persistentvolume "pvc-4fdb7fa7-fb43-4657-963e-4c4487e0f9d5" deleted
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl delete pv pvc-32612509-361e-4f6a-885e-b91802e4c742
persistentvolume "pvc-32612509-361e-4f6a-885e-b91802e4c742" deleted
``

Control that all items in dashboard/Persistent Volumes have been deleted.

### Stop Minikube
``
minikube stop
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> minikube stop
* Узел "minikube" останавливается ...
* Остановлено узлов: 1.
  ``

So, all my items have been removed in MiniKube.

## Restart Minikube
Started Docker Desktop
Started Oracle VM VirtualBox
In Windows PowerShell PS C:\Users\iurii>

### Start Minikube instance
``
minikube start --driver=virtualbox --no-vtx-check
``

Result:     
``
PS C:\Users\iurii> minikube start --driver=virtualbox --no-vtx-check
* minikube v1.28.0 на Microsoft Windows 10 Home Single Language 10.0.19044 Build 19044
* Используется драйвер virtualbox на основе существующего профиля
* Запускается control plane узел minikube в кластере minikube
* Перезагружается существующий virtualbox VM для "minikube" ...
* Found network options:
  - NO_PROXY=localhost,127.0.0.1,10.96.0.0/12,192.168.59.0/24,192.168.49.0/24,192.168.39.0/24
  - no_proxy=localhost,127.0.0.1,10.96.0.0/12,192.168.59.0/24,192.168.49.0/24,192.168.39.0/24
    ! This VM is having trouble accessing https://registry.k8s.io
* To pull new external images, you may need to configure a proxy: https://minikube.sigs.k8s.io/docs/reference/networking/proxy/
* Подготавливается Kubernetes v1.25.3 на Docker 20.10.20 ...
  - env NO_PROXY=localhost,127.0.0.1,10.96.0.0/12,192.168.59.0/24,192.168.49.0/24,192.168.39.0/24
  - Используется образ docker.io/kubernetesui/dashboard:v2.7.0
  - Используется образ docker.io/kubernetesui/metrics-scraper:v1.0.8
  - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
  - Используется образ k8s.gcr.io/metrics-server/metrics-server:v0.6.1
  - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
  - Используется образ gcr.io/k8s-minikube/storage-provisioner:v5
  - Используется образ k8s.gcr.io/ingress-nginx/controller:v1.2.1
* Компоненты Kubernetes проверяются ...
* Verifying ingress addon...
* Some dashboard features require the metrics-server addon. To enable all features please run:
        minikube addons enable metrics-server
* Включенные дополнения: storage-provisioner, default-storageclass, metrics-server, ingress, dashboard
* Готово! kubectl настроен для использования кластера "minikube" и "default" пространства имён по умолчанию
``

### Start minikube dashboard in a browser
``
minikube dashboard
``

Further, using IDEA's Terminal in D:\projects\examples\microservices\users\devops\K8s\helm>. 

## Upgrade helm's templates
### Upgrade _template-deployment.yaml
``
Deleted spec/templates/volumes
``

### Check the templates
``
helm lint .
``

Result.   
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm lint .
==> Linting .
[INFO] Chart.yaml: icon is recommended
1 chart(s) linted, 0 chart(s) failed
``
### Get info about generated deployments and services
``
helm template .
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm template .
---
# Source: users/templates/users-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
name: users-config
namespace: default
data:
postgres_db_host: users-pg-db-service
postgres_db_port: "54321"
---
# Source: users/templates/apps-service.yaml
apiVersion: v1
kind: Service
metadata:
name: users-app-service
namespace: default
spec:

selector:
app: users-app
ports:
- protocol: TCP
port: 8001
targetPort: 8080
---
# Source: users/templates/apps-service.yaml
apiVersion: v1
kind: Service
metadata:
name: users-pg-db-service
namespace: default
spec:
type: LoadBalancer
selector:
app: users-pg-db
ports:
- protocol: TCP
port: 54321
targetPort: 5432
nodePort: 30543
---
# Source: users/templates/apps-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
name: users-app-deployment
namespace: default
labels:
app: users-app
spec:
serviceName: users-app-service
replicas: 2
selector:
matchLabels:
app: users-app
template:
metadata:
labels:
app: users-app
spec:
containers:
- name: users-app
image: yuryli/k8s-users-app:11
ports:
- containerPort: 8080
resources:
requests:
memory: 256M  #256MB
cpu: 100m     #10% of total cpu capacity
limits:
memory: 512M  #512MB
cpu: 200m     #20% of total cpu capacity
env:              #indent 8 adds 8 spaces
- name: POSTGRES_DB_HOST
valueFrom:
configMapKeyRef:
key: postgres_db_host
name: users-config
- name: POSTGRES_DB_PORT
valueFrom:
configMapKeyRef:
key: postgres_db_port
name: users-config
---
# Source: users/templates/apps-deployment.yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
name: users-pg-db-deployment
namespace: default
labels:
app: users-pg-db
spec:
serviceName: users-pg-db-service
replicas: 2
selector:
matchLabels:
app: users-pg-db
template:
metadata:
labels:
app: users-pg-db
spec:
containers:
- name: users-pg-db
image: yuryli/k8s-users-pg-db:14.6
ports:
- containerPort: 5432
resources:
requests:
memory: 256M  #256MB
cpu: 100m     #10% of total cpu capacity
limits:
memory: 512M  #512MB
cpu: 200m     #20% of total cpu capacity
env:              #indent 8 adds 8 spaces
- name: POSTGRES_DB
value: users
- name: POSTGRES_USER
value: microuser
- name: POSTGRES_PASSWORD
value: microuser
- name: PGDATA
value: /var/lib/postgresql/data/pgdata
volumeMounts:
- mountPath: /var/lib/postgresql/data/
name: users-pg-db-volume
volumeClaimTemplates:
- metadata:
  name: users-pg-db-volume
  spec:
  accessModes:
  - ReadWriteOnce
    resources:
    requests:
    storage: 1Gi
---
# Source: users/templates/users-ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
name: users-ingress
namespace: default
spec:
rules:
- host: users.local
http:
paths:
- path: /
pathType: Prefix
backend:
service:
name: users-app-service
port:
number: 8001
``

### Install the apps by helm
``
helm install users-helm . -f values.yaml
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm install users-helm . -f values.yaml
W0126 12:13:19.141382   18016 warnings.go:70] unknown field "spec.serviceName"
NAME: users-helm
LAST DEPLOYED: Thu Jan 26 12:13:12 2023
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
``

### Create users_scheme schema by pgAdmin manually
``
CREATE SCHEMA users_scheme
AUTHORIZATION microuser;
COMMENT ON SCHEMA users_scheme
IS 'The main schema of microcervice "users" for users-pg-db-service in k8s minikube';
``

### Check upgrading
``
kubectl get all
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl get all
NAME                                        READY   STATUS    RESTARTS      AGE
pod/users-app-deployment-79c98f75cc-29wjg   1/1     Running   1 (11m ago)   23m
pod/users-app-deployment-79c98f75cc-88nrn   1/1     Running   1             23m
pod/users-pg-db-deployment-0                1/1     Running   0             23m
pod/users-pg-db-deployment-1                1/1     Running   0             22m

NAME                          TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)           AGE
service/kubernetes            ClusterIP      10.96.0.1        <none>        443/TCP           31d
service/users-app-service     ClusterIP      10.104.165.104   <none>        8001/TCP          23m
service/users-pg-db-service   LoadBalancer   10.103.24.202    <pending>     54321:30543/TCP   23m

NAME                                   READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/users-app-deployment   2/2     2            2           23m

NAME                                              DESIRED   CURRENT   READY   AGE
replicaset.apps/users-app-deployment-79c98f75cc   2         2         2       23m

NAME                                      READY   AGE
statefulset.apps/users-pg-db-deployment   2/2     23m
``

### Opening users-app in browser Google Chrome
``
http://users.local/swagger-ui/index.html
``

Result:     
``
Rest API of Users microservice
0.0.1
OAS3
/api-docs
Rest API of Users microservice as a demo project on Spring Boot
...
``

### Creating new user in browser Google Chrome
Works correctly.

### Creating new user by PostMan
Works correctly.

## Checking the changes result
### Stop Minikube
``
minikube stop
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> minikube stop
* Узел "minikube" останавливается ...
* Остановлено узлов: 1.
``

### Start Minikube instance
``
minikube start --driver=virtualbox --no-vtx-check
``

Result:     
``
The same
``

### Check installing
``
kubectl get all
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl get all
NAME                                        READY   STATUS    RESTARTS      AGE
pod/users-app-deployment-79c98f75cc-29wjg   1/1     Running   6 (12m ago)   99m
pod/users-app-deployment-79c98f75cc-88nrn   1/1     Running   3 (12m ago)   99m
pod/users-pg-db-deployment-0                1/1     Running   1             99m
pod/users-pg-db-deployment-1                1/1     Running   1 (33m ago)   98m

NAME                          TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)           AGE
service/kubernetes            ClusterIP      10.96.0.1        <none>        443/TCP           32d
service/users-app-service     ClusterIP      10.104.165.104   <none>        8001/TCP          99m
service/users-pg-db-service   LoadBalancer   10.103.24.202    <pending>     54321:30543/TCP   99m

NAME                                   READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/users-app-deployment   2/2     2            2           99m

NAME                                              DESIRED   CURRENT   READY   AGE
replicaset.apps/users-app-deployment-79c98f75cc   2         2         2       99m

NAME                                      READY   AGE
statefulset.apps/users-pg-db-deployment   2/2     99m
``

!!! After 30+ minutes of waiting.

### Check users_scheme schema by pgAdmin
Schema users_scheme exists with whole data.

### Getting users in browser Google Chrome
Works correctly.

### Getting users by PostMan
Works correctly.

Solved!