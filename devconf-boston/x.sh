#!/usr/bin/env bash

NARAYANA_VERSION=5.9.0.Final
LRA_JAR="narayana-full-${NARAYANA_VERSION}/rts/lra/lra-coordinator-swarm.jar"

java -jar $LRA_JAR -Dswarm.http.port=8080 -Dswarm.transactions.object-store-path=../parent &
java -jar $LRA_JAR -Dswarm.http.port=8081 -Dswarm.transactions.object-store-path=../subordinate &
java -jar hotel-service/target/lra-test-swarm.jar -Dswarm.http.port=8082 &
java -jar flight-service/target/lra-test-swarm.jar -Dswarm.http.port=8083 -Dlra.http.port=8081 &
java -jar trip-controller/target/lra-test-swarm.jar -Dswarm.http.port=8084 -Dlra.http.port=8080 &


#BOOKINGID=$(curl -X POST "http://localhost:8084/?hotelName=TheGrand&flightNumber1=BA123&flightNumber2=RH456" -sS | jq -r ".id")
#curl -X PUT http://localhost:8084/`urlencode $BOOKINGID` -sS | jq

#BOOKINGID=$(curl -X POST "http://localhost:8084/?hotelName=TheGrand&flightNumber1=BA123&flightNumber2=RH456" -sS | jq -r ".id")
#curl -X DELETE http://localhost:8084/`urlencode $BOOKINGID` -sS | jq

