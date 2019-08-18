
## Using STM to scale applications

The functionality of the demonstration application is very simple: it maintains a count of the number of
bookings made by REST clients, ie the *actor* accepts http POST messages and modifies an internal
counter and reports the current value back to the client.

There are three versions of the same application:

1. [a standard quarkus/vert.x application with no concurrency support](withoutSTM/README.md)
2. [a standard quarkus/vert.x application with STM support](withSTM/README.md)
3. [a standard quarkus/vert.x application with STM support running in a cloud environment](withSTM_on_openshift/README.md)

First build the 3 versions of the demonstration application:


```bash
mvn clean package
```

Now run the non STM version on quarkus in dev mode:

```bash
./mvnw quarkus:dev -f withoutSTM/pom.xml
```

The booking count should be zero. In another window type

```bash
curl http://localhost:8080/stm
```

NB to increment the counter send a post request to the resource (curl -X POST http://localhost:8080/stm).

Make multiple concurrent booking requests:

```bash
java -jar stress/target/codeone-stress-1.0.jar requests=100 parallelism=50 url=/stm
```

The booking count should be 100 times 50 = 5000 but since the resource has no concurrency
support the count will most likely be less than 5000:

```bash
curl http://localhost:8080/stm
```

Stop the application (type ctrl-C in the window running the mvnw quarkus:dev command).

Now run the STM version of the resource:

```bash
./mvnw quarkus:dev -f withSTM/pom.xml
```

and rerun the stress test:

```bash
java -jar stress/target/codeone-stress-1.0.jar requests=100 parallelism=50 url=/stm
```

Now the booking count should be 5000:

```bash
curl http://localhost:8080/stm
```

== Generating a native graal image

```bash
./mvnw clean package -Pnative -P withoutSTM/pom.xml
```

NB I cannot get this to work on my Fedora OS. The compilation fails with:

```
[INFO] [io.quarkus.creator.phase.nativeimage.NativeImagePhase] /usr/local/graalvm-ce-1.0.0-rc16/bin/native-image -J-Djava.util.logging.manager=org.jboss.logmanager.LogManager -J-Dio.netty.leakDetection.level=DIS
ABLED -J-Dvertx.disableDnsResolver=true -J-Dio.netty.noUnsafe=true --initialize-at-build-time= -H:InitialCollectionPolicy=com.oracle.svm.core.genscavenge.CollectionPolicy$BySpaceAndTime -jar codeone-without-stm-
quickstart-1.0-SNAPSHOT-runner.jar -J-Djava.util.concurrent.ForkJoinPool.common.parallelism=1 -H:FallbackThreshold=0 -H:+ReportExceptionStackTraces -H:+PrintAnalysisCallTree -H:-AddAllCharsets -H:EnableURLProtoc
ols=http -H:NativeLinkerOption=-no-pie -H:-SpawnIsolates -H:-JNI --no-server -H:-UseServiceLoaderFeature -H:+StackTrace
Error: Unrecognized option: --initialize-at-build-time=
```



== old instructions ==

...
and then try out each step starting with [a version without STM support](withoutSTM/README.md) which
shows up issues due to lack of concurrency protection.
Then try out the [version that adds STM support](withSTM/README.md).
And finally run a [version of the application](withSTM_on_openshift/README.md) that scales by
running on more than one JVM.

The instructions for each demo assume you are in the same directory as the demo. If you run the
code from this directory just specify which pom to use (ie -f directory/pom.xml).

Notice that the cloud based version of the application requires some prerequisite steps
for installing the OpenShift cloud environment and these are detailed in
the [README for that step](demo_with_STM_on_openshift/README.md).
