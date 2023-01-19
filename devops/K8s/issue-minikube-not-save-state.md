#issue-minikube-not-save-state

## Description.
Every time the minikube starts a new instance starts instead of exist instance starts in saved state.   
But not every time see Case 26.12.2022 below. In that case only db state has not been saved.

## Case 26.12.2022
### Start minikube
VirtualBox Manager has not started previously.    
At the line "* Перезагружается существующий virtualbox VM для "minikube" ..." the process has stuck.    
After 33 minutes of waiting I have started VirtualBox Manager manually which made to continue the process. 

``
minikube start --driver=virtualbox --no-vtx-check
``

Result:     
``
PS D:\projects\examples\microservices\users> minikube start --driver=virtualbox --no-vtx-check
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
    - Используется образ k8s.gcr.io/metrics-server/metrics-server:v0.6.1
    - Используется образ gcr.io/k8s-minikube/storage-provisioner:v5
    - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
    - Используется образ docker.io/kubernetesui/dashboard:v2.7.0
    - Используется образ k8s.gcr.io/ingress-nginx/controller:v1.2.1
    - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
    - Используется образ docker.io/kubernetesui/metrics-scraper:v1.0.8
* Компоненты Kubernetes проверяются ...
* Verifying ingress addon...
* Some dashboard features require the metrics-server addon. To enable all features please run:

        minikube addons enable metrics-server


* Включенные дополнения: storage-provisioner, default-storageclass, metrics-server, ingress, dashboard
* Готово! kubectl настроен для использования кластера "minikube" и "default" пространства имён по умолчанию

``

### Strange but on 26.12.2022 exist instance of minikube has started but with a new instance of database  
After creating db schema manually users-app starts correctly after a few restarts

``
PS D:\projects\examples\microservices\users> helm list
NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART                   APP VERSION
users-helm      default         2               2022-12-25 13:50:54.5740401 +0300 MSK   deployed        users-0.0.1-SNAPSHOT    0.0.1
``

``
PS D:\projects\examples\microservices\users> kubectl get deployment.apps
NAME                     READY   UP-TO-DATE   AVAILABLE   AGE
users-app-deployment     0/2     2            0           18h
users-pg-db-deployment   2/2     2            2           18h
``

``
PS D:\projects\examples\microservices\users> kubectl get pods
NAME                                     READY   STATUS             RESTARTS        AGE
users-app-deployment-58b778899c-4zwx2    0/1     Error              5 (2m58s ago)   18h
users-app-deployment-58b778899c-c9slv    0/1     CrashLoopBackOff   7 (2m39s ago)   18h
users-pg-db-deployment-bdc84b778-2fqnh   1/1     Running            1 (15h ago)     18h
users-pg-db-deployment-bdc84b778-pj7hh   1/1     Running            1 (15h ago)     18h
``

``
PS D:\projects\examples\microservices\users> kubectl get pods
NAME                                     READY   STATUS             RESTARTS      AGE
users-app-deployment-58b778899c-4zwx2    0/1     CrashLoopBackOff   5 (91s ago)   18h
users-app-deployment-58b778899c-c9slv    1/1     Running            8 (4m ago)    18h
users-pg-db-deployment-bdc84b778-2fqnh   1/1     Running            1 (15h ago)   18h
users-pg-db-deployment-bdc84b778-pj7hh   1/1     Running            1 (15h ago)   18h
``

``
PS D:\projects\examples\microservices\users> kubectl get pods
NAME                                     READY   STATUS             RESTARTS        AGE
users-app-deployment-58b778899c-4zwx2    0/1     CrashLoopBackOff   5 (2m40s ago)   18h
users-app-deployment-58b778899c-c9slv    1/1     Running            8 (5m9s ago)    18h
users-pg-db-deployment-bdc84b778-2fqnh   1/1     Running            1 (15h ago)     18h
users-pg-db-deployment-bdc84b778-pj7hh   1/1     Running            1 (15h ago)     18h
``

``
PS D:\projects\examples\microservices\users> kubectl get pods
NAME                                     READY   STATUS    RESTARTS        AGE
users-app-deployment-58b778899c-4zwx2    1/1     Running   6 (4m22s ago)   18h
users-app-deployment-58b778899c-c9slv    1/1     Running   8 (6m51s ago)   18h
users-pg-db-deployment-bdc84b778-2fqnh   1/1     Running   1 (15h ago)     18h
users-pg-db-deployment-bdc84b778-pj7hh   1/1     Running   1 (15h ago)     18h
``