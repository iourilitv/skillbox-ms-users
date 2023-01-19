# Issue error if helm send a number value into ConfigMap

## Initial values in ConfigMap file
Resource: devops/K8s/helm/templates/users-config.yaml   
``
data:
  postgres_db_host: "users-pg-db-service"
  postgres_db_port: "54321"
``

## Add variables into helm's values file
Resource: devops/K8s/helm/values.yaml
``
db:
  service:
    name: users-pg-db-service
    port: 54321
``

## When replace the value of a port by variable from helm's values file
``
data:
postgres_db_host: {{ .Values.db.service.name }}
postgres_db_port: {{ .Values.db.service.port }}
``

then we'll get error at postgres_db_port property
``
PS D:\projects\examples\microservices\users\devops\K8s> helm install users-helm helm/
Error: INSTALLATION FAILED: ConfigMap in version "v1" cannot be handled as a ConfigMap: json: cannot unmarshal number into Go struct field ConfigMap.data of type string
``

## Solution
### Way 1. Wrap port value with '' or ""
``
data:
...
postgres_db_port: '{{ .Values.db.service.port }}'
``

### Way 2. The Best solution is to add  | quote instead of ''  
Source: https://helm.sh/docs/chart_template_guide/control_structures/#controlling-whitespace     

``
data:
...
postgres_db_port: {{ .Values.apps.db.service.port | quote }}
``

### Confirmation that it works correctly
In minikube dashboard, Confirm And Storage > Config Maps > users-config
http://127.0.0.1:63951/api/v1/namespaces/kubernetes-dashboard/services/http:kubernetes-dashboard:/proxy/#/configmap/default/users-config?namespace=default
``
{
"postgres_db_host": "users-pg-db-service",
"postgres_db_port": "54321"
}
``