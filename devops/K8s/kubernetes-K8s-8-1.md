# kubernetes (K8s). Part 8-1
Resolving issue-minikube-not-save-state.md.     
!!! Does not work!!!        
Apps deploy correctly but data does not save.

Source: https://kubernetes.io/docs/tasks/configure-pod-container/configure-persistent-volume-storage/       
Additional source: https://akomljen.com/kubernetes-persistent-volumes-with-deployment-and-statefulset/

## Create PersistentVolume
### Create users-pg-db-volume.yaml file
Create new file devops/K8s/helm/volumes/users-pg-db-volume.yaml

### Apply PersistentVolume
``
kubectl apply -f volumes/users-pg-db-volume.yaml
``

Result:         
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl apply -f volumes/users-pg-db-volume.yaml
persistentvolume/users-pg-db-volume created
``

### Check PersistentVolume
``
kubectl get pv users-pg-db-volume
``

Result:          
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl get pv users-pg-db-volume
NAME                 CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS      CLAIM   STORAGECLASS   REASON   AGE
users-pg-db-volume   2Gi        RWO            Retain           Available           manual                  77s
``

## Create PersistentVolumeClaim
### Create users-pg-db-claim.yaml file
Create new file devops/K8s/helm/volumes/users-pg-db-claim.yaml

### Apply PersistentVolumeClaim
``
kubectl apply -f volumes/users-pg-db-claim.yaml
``

Result:         
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl apply -f volumes/users-pg-db-claim.yaml
persistentvolumeclaim/users-pg-db-claim created
``

### Check PersistentVolume again
STATUS has been changed from Available to Bound and Claim is specified now      
``
kubectl get pv users-pg-db-volume
``

Result:          
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl get pv users-pg-db-volume
NAME                 CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM                       STORAGECLASS   REASON   AGE
users-pg-db-volume   2Gi        RWO            Retain           Bound    default/users-pg-db-claim   manual                  4m55s
``

### Check PersistentVolumeClaim
``
kubectl get pvc users-pg-db-claim
``

Result:          
``
PS D:\projects\examples\microservices\users\devops\K8s\helm> kubectl get pvc users-pg-db-claim
NAME                STATUS   VOLUME               CAPACITY   ACCESS MODES   STORAGECLASS   AGE
users-pg-db-claim   Bound    users-pg-db-volume   2Gi        RWO            manual         2m5s
``


## Clean up all
### Delete Pod
``
kubectl delete pvc users-pg-db-xxxx
``

Result:     
``

``

### Delete Claim
``
kubectl delete pvc users-pg-db-claim
``

Result:     
``
persistentvolumeclaim "users-pg-db-claim" deleted
``

### Delete Volume
``
kubectl delete pvc users-pg-db-volume
``

Result:     
``
persistentvolume "users-pg-db-volume" deleted
``
