# Issue during the ingress nginx installation

## Trying to install ingress-nginx with automatic downloading required images from web source
### Start installation process

``
minikube addons enable ingress
``

Result after about 10 - 20 minutes:   
``
PS D:\projects\examples\microservices\users> minikube addons enable ingress
* ingress is an addon maintained by Kubernetes. For any concerns contact minikube on GitHub.
  You can view the list of minikube maintainers at: https://github.com/kubernetes/minikube/blob/master/OWNERS
    - Используется образ k8s.gcr.io/ingress-nginx/controller:v1.2.1
    - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
    - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
* Verifying ingress addon...

X Exiting due to MK_ADDON_ENABLE: run callbacks: running callbacks: [waiting for app.kubernetes.io/name=ingress-nginx pods: timed out waiting for the condition]
*
╭────────────────────────────────────────────────────────────────────────────────────────────────────────────╮
│                                                                                                            │
│    * If the above advice does not help, please let us know:                                                │
│      https://github.com/kubernetes/minikube/issues/new/choose                                              │
│                                                                                                            │
│    * Please run `minikube logs --file=logs.txt` and attach logs.txt to the GitHub issue.                   │
│    * Please also attach the following file to the GitHub issue:                                            │
│    * - C:\Users\iurii\AppData\Local\Temp\minikube_addons_1aef1703bf1ac9321df730b74e6caf5a70b2183b_0.log    │
│                                                                                                            │
╰────────────────────────────────────────────────────────────────────────────────────────────────────────────╯
``

## Reason
Timeout exceeded because minikube's network adapter speed is less than 0,5 Mbit/c


## Solution
Pull required images manually into minikube's docker

### Pull the images manually into minikube's docker
Set minikube's docker for using.  
In PowerShell  
``
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
``

### Pull image of ingress-nginx/controller
``
docker pull k8s.gcr.io/ingress-nginx/controller:v1.2.1
``

Result:   
``
...
Digest: sha256:5516d103a9c2ecc4f026efbd4b40662ce22dc1f824fb129ed121460aaa5c47f8
Status: Downloaded newer image for k8s.gcr.io/ingress-nginx/controller:v1.2.1
k8s.gcr.io/ingress-nginx/controller:v1.2.1

``

### Pull image of ingress-nginx/kube-webhook-certgen
This has been downloaded automatically within the installation
``
docker pull k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
``

Result:   
``
...
v1.1.1: Pulling from ingress-nginx/kube-webhook-certgen
Digest: sha256:64d8c73dca984af206adf9d6d7e46aa550362b1d7a01f3a0a91b20cc67868660
Status: Downloaded newer image for k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1


``

### Trying to repeat the installation
``
minikube addons enable ingress
``

Result:   
``  
PS D:\projects\examples\microservices\users> minikube addons enable ingress
* ingress is an addon maintained by Kubernetes. For any concerns contact minikube on GitHub.
  You can view the list of minikube maintainers at: https://github.com/kubernetes/minikube/blob/master/OWNERS
  - Используется образ k8s.gcr.io/ingress-nginx/controller:v1.2.1
  - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
  - Используется образ k8s.gcr.io/ingress-nginx/kube-webhook-certgen:v1.1.1
* Verifying ingress addon...
* The 'ingress' addon is enabled

``