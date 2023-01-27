# kubernetes (K8s). Part 8-2
Starting the deployments from the beginning to resolve issue-minikube-not-save-state.md.     
By upgrading exist users-helm.
!!! Does not work!!!        
Apps deploy correctly but data does not save.

Sources: https://www.bmc.com/blogs/kubernetes-postgresql/;
https://kubernetes.io/docs/tutorials/stateful-application/basic-stateful-set/

## Restart Minikube
### Start Docker Desktop
### Start Oracle VM VirtualBox
### Run Windows PowerShell      
Result:     
``
PS C:\Users\iurii>
``

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
    - Используется образ k8s.gcr.io/ingress-nginx/controller:v1.2.1
    - Используется образ k8s.gcr.io/metrics-server/metrics-server:v0.6.1
    - Используется образ gcr.io/k8s-minikube/storage-provisioner:v5
    - Используется образ docker.io/kubernetesui/dashboard:v2.7.0
    - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
    - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
    - Используется образ docker.io/kubernetesui/metrics-scraper:v1.0.8
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
Replaced with: kind: {{ .kind }}

Added to spec/:
1.
serviceName: {{ .service.name }}
2.
{{- end }}
{{- if .volumeClaimTemplates }}
volumeClaimTemplates:
{{ toYaml .volumeClaimTemplates | indent 2 }}
``
### Upgrade values.yaml
``
Added to backend:
kind: Deployment

Added to db:
1. kind: StatefulSet
2.     volumeClaimTemplates:
  - metadata:
    name: users-pg-db-volume
    spec:
    accessModes: [ "ReadWriteOnce" ]
    resources:
    requests:
    storage: 1Gi
Replaced with: container/volumeMounts:
    - mountPath: "/var/lib/postgresql/data/"
``
### Check the templates
``
helm lint ./devops/K8s/helm
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
### Upgrade the apps by helm
``
helm upgrade users-helm . -f values.yaml
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm upgrade users-helm . -f values.yaml
W0125 15:59:11.457824   18252 warnings.go:70] unknown field "spec.serviceName"
Release "users-helm" has been upgraded. Happy Helming!
NAME: users-helm
LAST DEPLOYED: Wed Jan 25 15:59:01 2023
NAMESPACE: default
STATUS: deployed
REVISION: 2
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
NAME                                        READY   STATUS    RESTARTS         AGE
pod/users-app-deployment-79c98f75cc-jmbp5   1/1     Running   56 (16m ago)     25d
pod/users-app-deployment-79c98f75cc-t8l6x   1/1     Running   62 (6m40s ago)   25d
pod/users-pg-db-deployment-0                1/1     Running   0                42m
pod/users-pg-db-deployment-1                1/1     Running   0                40m
NAME                          TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)           AGE
service/kubernetes            ClusterIP      10.96.0.1       <none>        443/TCP           31d
service/users-app-service     ClusterIP      10.97.81.0      <none>        8001/TCP          25d
service/users-pg-db-service   LoadBalancer   10.104.72.211   <pending>     54321:30543/TCP   25d
NAME                                   READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/users-app-deployment   2/2     2            2           25d
NAME                                              DESIRED   CURRENT   READY   AGE
replicaset.apps/users-app-deployment-79c98f75cc   2         2         2       25d
NAME                                      READY   AGE
statefulset.apps/users-pg-db-deployment   2/2     42m
``

### Opening users-app in browser
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

### Check users_scheme schema by pgAdmin
The same result - Schema users_scheme does not exist.
