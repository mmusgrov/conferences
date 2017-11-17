
mvn clean install

mvn -f vertx/pom.xml vertx:debug -Ddebug.suspend=false -DrunArgs.flights=true
mvn -f vertx/pom.xml vertx:run   -Ddebug.suspend=false -DrunArgs.flights=false

java -jar swarm/target/stm-warm-demo-swarm.jar -Dswarm.http.port=8081

curl -X POST http://localhost:8080/api
curl -X POST http://localhost:8081/api

curl -X GET http://localhost:8080/api
curl -X GET http://localhost:8081/api
