persistentVolumeClaimDef() {
#https://docs.openshift.com/container-platform/3.11/dev_guide/persistent_volumes.html#dev-guide-persistent-volumes
cat << EOF
apiVersion: "v1"
kind: "PersistentVolumeClaim"
metadata:
  name: "${1}"
spec:
  accessModes:
    - "ReadWriteOnce"
  resources:
    requests:
      storage: "100Mi"
  persistentVolumeReclaimPolicy: Retain
EOF
}

persistentVolumeClaimDef "stm-logs" | oc create -f -

#oc volume dc/withoutstm --add -t persistentVolumeClaim --claim-name stm-logs -m /data
