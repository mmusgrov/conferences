
rm -rf ~/.minishift ~/.kube
minishift start --vm-driver=virtualbox
oc login -u developer -p developer
oc delete project stmdemo
oc new-project stmdemo
../pv.sh

export APP=with-persistence
oc new-build --binary --name=${APP} -l app=${APP}
oc start-build ${APP} --from-dir=. --follow
oc new-app ${APP} -l app=${APP}

oc set volume dc/${APP} --add --name volume-1 --type pvc --claim-name stm-logs --mount-path /deployments/data
#oc set volume dc/${APP} --add -t persistentVolumeClaim --claim-name stm-logs -m /data
oc expose service ${APP}

curl -XPOST http://with-persistence-stmdemo.192.168.99.100.nip.io/stm
curl -XPOST http://localhost:8080/stm
java -jar ../stress/target/codeone-stress-1.0.jar requests=100 parallelism=50 url=/stm 
# ln -s tmp deployments
