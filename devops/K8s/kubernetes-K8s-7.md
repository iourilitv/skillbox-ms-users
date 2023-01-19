# kubernetes (K8s). Part 7
Using namespaces.

## Creating namespaces
### Create namespace users-dev manually
``
kubectl create namespace users-dev 
``

Result:         
``
namespace/users-dev created
``

### Check namespaces
``
kubectl get namespaces
``

Result:     
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl get namespaces
NAME                   STATUS   AGE
default                Active   2d19h
ingress-nginx          Active   2d19h
kube-node-lease        Active   2d19h
kube-public            Active   2d19h
kube-system            Active   2d19h
kubernetes-dashboard   Active   2d19h
users-dev              Active   77s
``

### Delete namespace (if required)
``
kubectl delete namespace users-dev
``

Result:     
``
namespace "users-dev" deleted
``

### Create namespace users-test and other ones by yaml-file
Create file namespaces.yaml

``
kubectl apply -f namespaces.yaml
``

Result:     
``
namespace/users-test created
namespace/users-dev created
namespace/users-prod created
``

## Update helm templates etc. with namespaces using
### Update files:
_template-deployment.yaml
_template-service.yaml
users-config.yaml
users-ingress.yaml
values.yaml

### Created values-test file
### Added users-test.local into ./etc/hosts file
192.168.59.102 users-test.local
192.168.59.102 users-dev.local
192.168.59.102 users-prod.local

## Deploying apps with their namespaces
### Deploy apps with users-test namespace
``
helm install users-test-helm . -f values-test.yaml
``

Result:  
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm upgrade users-test-helm . -f values-test.yaml
Error: UPGRADE FAILED: failed to create resource: Service "users-pg-db-service" is invalid: spec.ports[0].nodePort: Invalid value: 30543: provided port is already allocated
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm upgrade users-test-helm . -f values-test.yaml
Release "users-test-helm" has been upgraded. Happy Helming!
NAME: users-test-helm
LAST DEPLOYED: Wed Dec 28 12:51:01 2022
NAMESPACE: default
STATUS: deployed
REVISION: 4
TEST SUITE: None
``

### Repeating.
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm delete users-test-helm
release "users-test-helm" uninstalled
``

Or after deleted and repeated helm.      
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> helm install users-test-helm . -f values-test.yaml
NAME: users-test-helm
LAST DEPLOYED: Wed Dec 28 13:02:15 2022
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
``


