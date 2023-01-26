# Issue "A pod created with status ImagePullBackOff and is not ready"

## If Create deployments.app with single container using exist image in Docker
Using local image

``
kubectl create deployment users-pg-db --image=postgres:14.6
``

### Then getting list of pods:

``
kubectl get pods
``

result:

``
NAME                           READY   STATUS             RESTARTS   AGE
users-pg-db-566c784467-b6jz7   0/1     ImagePullBackOff   0          22m
``

More details: devops/K8s/logs/issue-pod-status-ImagePullBackOff.md.log


## Solution. !!!DID NOT HELP!!!
Push images to remote public repositories on DockerHub.

Source of example: https://www.youtube.com/watch?v=7iJLvondL5U&list=PLg5SS_4L6LYvN1RqaVesof8KAf-02fJSi&index=6&t=376s

### Create link to exist local image before pushing
``
docker tag postgres:14.6 yuryli/k8s-users-pg-db:14.6
``

### Sign in my account on DockerHub

``
docker login
``

Result:

``
Authenticating with existing credentials...
Login Succeeded
Logging in with your password grants your terminal complete access to your account.
For better security, log in with a limited-privilege personal access token. Learn more at https://docs.docker.com/go/access-tokens/
``

### Push images to a new repository on DockerHub

``
docker push yuryli/k8s-users-pg-db:14.6
``

Result:

``
...
b5ebffba54d3: Mounted from library/postgres
14.6: digest: sha256:3e87ab1ea1fe4fa1d297c5718534d3e472447deee6ff1f8f03accec05c9b7c25 size: 3039
``

### Pull manually using minikube's docker engine
In Windows PowerSell

``
docker pull yuryli/k8s-users-pg-db:14.6
``

Result:

``
PS C:\WINDOWS\system32> docker pull yuryli/k8s-users-pg-db:14.6
14.6: Pulling from yuryli/k8s-users-pg-db
025c56f98b67: Already exists
26dc25c16f4e: Already exists
a032d8a894de: Already exists
40dba7d35750: Already exists
8ebb44a56070: Already exists
813fd6cf203b: Already exists
7024f61bf8f5: Already exists
23f986b322e8: Already exists
9f76961a3266: Pull complete
25ab50475209: Pull complete
6e14c5d69b41: Pull complete
61c4089902cf: Pull complete
a285fd6fb063: Pull complete
Digest: sha256:3e87ab1ea1fe4fa1d297c5718534d3e472447deee6ff1f8f03accec05c9b7c25
Status: Downloaded newer image for yuryli/k8s-users-pg-db:14.6
docker.io/yuryli/k8s-users-pg-db:14.6
``

Getting list of images.

``
PS C:\WINDOWS\system32> docker images --all
REPOSITORY                                 TAG       IMAGE ID       CREATED         SIZE
yuryli/k8s-users-pg-db                     14.6      3dfb9f0162b0   2 weeks ago     376MB
registry.k8s.io/kube-apiserver             v1.25.3   0346dbd74bcb   2 months ago    128MB
registry.k8s.io/kube-controller-manager    v1.25.3   603999231275   2 months ago    117MB
registry.k8s.io/kube-scheduler             v1.25.3   6d23ec0e8b87   2 months ago    50.6MB
registry.k8s.io/kube-proxy                 v1.25.3   beaaf00edd38   2 months ago    61.7MB
kubernetesui/dashboard                     <none>    07655ddf2eeb   3 months ago    246MB
registry.k8s.io/pause                      3.8       4873874c08ef   6 months ago    711kB
registry.k8s.io/etcd                       3.5.4-0   a8a176a5d5d6   6 months ago    300MB
kubernetesui/metrics-scraper               <none>    115053965e86   6 months ago    43.8MB
registry.k8s.io/coredns/coredns            v1.9.3    5185b96f0bec   6 months ago    48.8MB
k8s.gcr.io/metrics-server/metrics-server   <none>    e57a417f15d3   10 months ago   68.8MB
k8s.gcr.io/pause                           3.6       6270bb605e12   16 months ago   683kB
gcr.io/k8s-minikube/storage-provisioner    v5        6e38f40d628d   21 months ago   31.5MB
``

Then.

``
PS C:\WINDOWS\system32> kubectl get pods
NAME                          READY   STATUS             RESTARTS     AGE
users-pg-db-844744fc4-7kszw   0/1     CrashLoopBackOff   1 (5s ago)   21s
PS C:\WINDOWS\system32> kubectl get pods
NAME                          READY   STATUS             RESTARTS      AGE
users-pg-db-844744fc4-7kszw   0/1     CrashLoopBackOff   3 (31s ago)   103s
PS C:\WINDOWS\system32> kubectl get deployments
NAME          READY   UP-TO-DATE   AVAILABLE   AGE
users-pg-db   0/1     1            0           23m
PS C:\WINDOWS\system32> kubectl delete deployment users-pg-db
deployment.apps "users-pg-db" deleted
PS C:\WINDOWS\system32> kubectl get deployments
No resources found in default namespace.
``

