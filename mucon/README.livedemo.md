
# creating a new maven project:

mvn io.fabric8:vertx-maven-plugin:1.0.7:setup -DvertxVersion=3.4.2 -Ddependencies=web
io.narayana
mucon
1.0
io.narayana.mucon.FlightVerticle

edit the start method
        Router router = Router.router(vertx);

        router.post("/api").handler(request -> {
            request.response().end(Thread.currentThread.getName());
        });

        // create the http server passing in the router as the handler
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);

mvn compile vertx:run

curl -X POST http://localhost:8080/api

Create FlightService interface and FlightServiceImpl
create a main method:
  initializes a FlightServiceImpl
  deploys a verticle (Vertx.vertx().deployVerticle(FlightVerticle.class.getName())
  * remember to define the vertx.launcher property in the pom 
    you will get an NPE otherwise
    the vertx:run command will hang unless you manually deploy the verticle in the main
update the route handler to make and report the booking

curl -X POST http://localhost:8080/api
  but this is only serviced by a single thread - actors are designed for parallelism
Lets fix that by deploying 10 instances of the verticle:

  ...deployVerticle(FlightVerticle.class.getName(), new DeploymentOptions().setInstances(10));

now we can see the workload being serviced by different threads
and we can test that by running lots of parallel requests:

Build and run the stress test
  mvn package -f stress/pom.xml   
  java -jar stress/target/stress-1.0.jar requests=100 parallelism=50 url=/api
Hopefully we will see that none of the 5000 requests failed 

But now if we query the number of bookings they are less thatn 5000 because there is no synchronization
between the verticles.

Lets fix that using STM:
Add the STM and jboss logging dependencies to the pom:
    org.jboss.narayana.stm:stm:5.6.4.Final
    org.jboss.logging:jboss-logging:3.3.1.Final
  * make sure it is added to the build dependencies and NOT the dependencyManagement section *


resync the pom in the ide to re-index (might have to add the pom via the far righthand pane
will need to restart the mvn vertx:run command to pick up the pom changes

@Transactional on the FlightService interface
@State @ReadLock @WriteLock on the impl

Create an STM container to manage the FlightService memory in main
Ensure that each verticle clones the object in the start method:
   * important make sure the proxy is the second parameter *
   FlightService clone = container.clone(new FlightServiceImpl(), service);

curl -X POST http://localhost:8080/api

if the booking countes don't change you probably have new FlightServiceImpl(), service in the wrong order

Now rerun the stress test:
java -jar stress/target/stress-1.0.jar requests=100 parallelism=50 url=/api
curl -X POST http://localhost:8080/api

