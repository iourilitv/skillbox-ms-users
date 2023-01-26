# Issue during creating minikube using virtualbox driver

## If Start cluster minikube
``
minikube start --driver=virtualbox
``

### Then get the warning:

``

C:\Users\iurii>minikube start --driver=virtualbox
* minikube v1.28.0 на Microsoft Windows 10 Home Single Language 10.0.19044 Build 19044
* Используется драйвер virtualbox на основе конфига пользователя
* Downloading VM boot image ...
  > minikube-v1.28.0-amd64.iso....:  65 B / 65 B [---------] 100.00% ? p/s 0s
  > minikube-v1.28.0-amd64.iso:  274.45 MiB / 274.45 MiB  100.00% 4.46 MiB p/
* Запускается control plane узел minikube в кластере minikube
* Creating virtualbox VM (CPUs=2, Memory=3000MB, Disk=20000MB) ...
  ! StartHost failed, but will try again: creating host: create: precreate: This computer doesn't have VT-X/AMD-v enabled. Enabling it in the BIOS is mandatory
* Creating virtualbox VM (CPUs=2, Memory=3000MB, Disk=20000MB) ...
* Failed to start virtualbox VM. Running "minikube delete" may fix it: creating host: create: precreate: This computer doesn't have VT-X/AMD-v enabled. Enabling it in the BIOS is mandatory

X Exiting due to HOST_VIRT_UNAVAILABLE: Failed to start host: creating host: create: precreate: This computer doesn't have VT-X/AMD-v enabled. Enabling it in the BIOS is mandatory
* Предложение: Virtualization support is disabled on your computer. If you are running minikube within a VM, try '--driver=docker'. Otherwise, consult your systems BIOS manual for how to enable virtualization.
* Related issues:
    - https://github.com/kubernetes/minikube/issues/3900
    - https://github.com/kubernetes/minikube/issues/4730

``

## Solution. 
### Start cluster minikube with no-vtx-check attribute
``
minikube start --driver=virtualbox --no-vtx-check
``