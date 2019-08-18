
../mvnw package
../mvnw quarkus:dev
curl -XPOST http://localhost:8080/stm
java -jar ../stress/target/codeone-stress-1.0.jar requests=100 parallelism=50 url=/stm
curl -XPOST http://localhost:8080/stm
