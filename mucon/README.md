
## Adding more threads to service a workload

```bash
mvn exec:java -Pvolatile -Dport=8080 -f flight/pom.xml

curl -X POST http://localhost:8080/api/flight/BA123
```

Observe that each time the request is issued it is serviced by a different thread (watch the threadId field change).

## Adding more JVMs to service a workload:

This demo shows how different JVMs can share the same transactional memory. We show how
to run and scale the services using the minishift container platform.

Install minishift:

Download the [relevant binary from](https://github.com/minishift/minishift/releases) and add
the minishift executable to the path, on linux for example

```bash
export PATH=<install location>/minishift-1.7.0-linux-amd64:$PATH
minishift start --vm-driver=virtualbox # or whatever hypervisor you are using
minishift console # opens the openshift web console
oc login -u developer -p developer
oc new-project stmdemo
```

The service that we are about to deploy to minishift uses persistant storage so that any data
survives if a pod crashes. It also needs to be shared by different pods so that the transactional
memory can be shared between different JVMs. Go to the minishift console and create a
"Persistent Volume Claim" by clicking on the "Storage" menu option on the left hand pane of the console.

Give it the name "stm-vertx-demo-flight-logs", capacity 1GiB and RWO (Read-Write-Once) Access Mode.
Now deploy the service:

```bash
mvn fabric8:deploy -Popenshift -f flight/pom.xml
```

and create a flight booking:

```bash
curl -X POST http://stm-vertx-demo-flight-stmdemo.192.168.99.100.nip.io/api/flight/BA123
```

If you periodically query the flight status you should see it being serviced on different threads
[just as in the earlier demo](adding-more-threads-to-service-a-workload):

```bash
curl -X GET  http://stm-vertx-demo-flight-stmdemo.192.168.99.100.nip.io/api/flight
```

Now go back to the console, locate the flight deployment (Deployments -> stm-vertx-demo-flight -> #1)
and scale up the number of pods to 2.
Now create some more flight bookings and observe that the requests are serviced by different pods (watch the hostId field change).


## Composing STM objects

start the trip verticle which manages theatre, taxi and train bookings:
```bash
mvn clean compile exec:java -Ptrip -f trip/pom.xml
```
book a theatre and taxi:
```bash
curl -X POST http://localhost:8080/api/trip/Apollo/TaxiFirm
```
book a theatre and taxi. Abort the taxi and book a train instead:
```bash
curl -X POST http://localhost:8080/api/trip/Apollo/fail_TaxiFirm/train
```
book a theatre and taxi. Abort the theatre booking and observe that the taxi is cancelled
```bash
curl -X POST http://localhost:8080/api/trip/Apollofail/XYZ
```

