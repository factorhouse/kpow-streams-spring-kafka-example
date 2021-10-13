# Monitor Spring Kafka Applications with kPow

![kpow-kafka-streams-viz](https://user-images.githubusercontent.com/2832467/137060506-4d7ff311-29da-42ed-8e14-97ccfea6f7d3.png)

Integrated [Confluent Order Service](https://github.com/confluentinc/streaming-ops/tree/main/apps/microservices-orders/orders-service) Spring Kafka example application with the [kPow Streams Agent](https://github.com/operatr-io/kpow-streams-agent).

Run this project with the original instructions below, we have integrated the kPow Agent. You will see log-lines like:

```
kPow: sent [370] streams metrics for application.id OrdersService
```

Once started, run kPow with the target cluster and navigate to 'Streams' to view the live topology and metrics.

### Quickstart

This project uses kpow/local to start a local 3-node Kafka cluster with kPow, we then build an Order Service JAR and run it with the same Cluster.


-- Original Project Readme Follows --

## Orders Service

This is a re-write of the OrdersService found in the Confluent [Microservices Demos examples](https://github.com/confluentinc/kafka-streams-examples/tree/6.0.0-post/src/main/java/io/confluent/examples/streams/microservices)
using the Spring Framework.

## Building

The project contains a `Makefile` for managing build and package.

|            |                                           |
|------------|-------------------------------------------|
| `make clean` |will clean build artifacts|
| `make test`  |will run provide unit and integration tests|
| `make build` |will build a single shadow jar for running the application|
| `make package` |will build a Docker image|
| `make publish` |will build and publish the Docker image to the cnfldemos Confluent Docker Hub repository (proper credentials required)|

## Configuring

This application builds on the Spring Framework and uses [Spring Application Property Files](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-application-property-files).
Configuration can be overridden using many methods [(docs)](https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/html/boot-features-external-config.html).
