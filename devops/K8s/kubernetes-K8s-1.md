# kubernetes (K8s). Part 1

## Additional sources:
2-K8s - Поднятие простого Локального K8s Cluster на Windows: https://www.youtube.com/watch?v=WAIrMmCQ3hE&list=LL&index=2

## Download and install 3 required elements:
### VirtualBox (Virtual machine)
Download from https://download.virtualbox.org/virtualbox/7.0.4/VirtualBox-7.0.4-154605-Win.exe

Install VirtualBox-7.0.4-154605-Win.exe.
Here is installed without Python binding

### Kubectl(Cluster management service)
Download from https://kubernetes.io/docs/tasks/tools/install-kubectl-windows.
Install using instructions on that page. 

### Minikube(Cluster Single-Node - Master and Worker nodes in one for tests and education)
Download from https://kubernetes.io/docs/tasks/tools/install-minikube/.
Install using instructions on that page. 

### Add paths for kubectl and minikube into System variables Path.
D:\Software\K8s;

C:\Program Files\Kubernetes\Minikube

## Minikube starting
## Start cluster minikube. For restart the exists as well
``
minikube start --driver=virtualbox --no-vtx-check
``

or set virtualbox driver as default previously (didn't try here)
### To make virtualbox the default driver:
``
minikube config set driver virtualbox
``
### When minikube installs enable addons metrics-server for dashboard.
Some dashboard features require the metrics-server addon. To enable all features please run:

``
minikube addons enable metrics-server
``

## Minikube cluster operation
### Get statuses
``
kubectl get componentstatuses
``

result:

``
Warning: v1 ComponentStatus is deprecated in v1.19+
NAME                 STATUS    MESSAGE                         ERROR
scheduler            Healthy   ok
controller-manager   Healthy   ok
etcd-0               Healthy   {"health":"true","reason":""}
``

### Get cluster info
``
kubectl cluster-info
``

result:

``
Kubernetes control plane is running at https://192.168.59.100:8443
CoreDNS is running at https://192.168.59.100:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

---

PS C:\WINDOWS\system32> kubectl cluster-info
Kubernetes control plane is running at https://192.168.59.102:8443
CoreDNS is running at https://192.168.59.102:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy
``

### Get cluster nodes list
``
kubectl get nodes
``

result:

``
NAME       STATUS   ROLES           AGE   VERSION
minikube   Ready    control-plane   43m   v1.25.3
``

### Get cluster pods list
``
kubectl get pods
``

result:

``
No resources found in default namespace.
``

## Create containers using docker of minikube (Required for image building. Not used here)
Source: https://minikube.sigs.k8s.io/docs/tutorials/docker_desktop_replacement/

!!! Doesn't work here!

``
& minikube docker-env --shell powershell | Invoke-Expression
``

Works.

``
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
``

## Create deployments.app(users-pg-db) with single container using exist image in Docker 
ATTENTION! You should use an image which have been pushed into remote repository on DockerHub previously!  
See the issue "A pod created with status ImagePullBackOff and is not ready"
devops/K8s/issue-pod-status-ImagePullBackOff.md

``
kubectl create deployment users-pg-db --image=yuryli/k8s-users-pg-db:14.6
``

result:

``
deployment.apps/users-pg-db created
``




## ------------- Additional ----------------------

## Locations
### Config file
C:\Users\iurii\.kube\config
### Minikube data location
C:\Users\iurii\.minikube\machines\minikube


## Come into minikube cluster
``
minikube ssh
``

Inside minikube.

to check execute the follow command:
``
pwd
``

result: /home/docker

To exit execute the follow command:
``
exit
``
