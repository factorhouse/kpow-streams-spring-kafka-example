# Monitor Spring Kafka Applications with Kpow

![streams-topology-usage](https://github.com/user-attachments/assets/57c6549a-a408-4604-af64-068d2ee73d22)


Integrated [Confluent Order Service](https://github.com/confluentinc/streaming-ops/tree/main/apps/microservices-orders/orders-service) Spring Kafka example application with the [Kpow Streams Agent](https://github.com/factorhouse/kpow-streams-agent).

Run this project with the instructions below, we have integrated the Kpow Agent. You will see log-lines like:

```
Kpow: sent [370] streams metrics for application.id OrdersService
```

Once started, run Kpow with the target cluster and navigate to 'Streams' to view the live topology and metrics.

### Quickstart

1. Start a 3-Node Kafka Cluster and Kpow with [Kpow Local](https://github.com/factorhouse/kpow-local).
2. Build the Order Service JAR with `make test`
3. Run the Order Service JAR with `java -jar build/libs/orders-service-10.0.8.jar`
4. Navigate to localhost:3000 > Streams > OrdersService (can take 1-2 minutes to appear)

## How We Integrated WordCount Streams with the Kpow Agent

### Get the Kpow Streams Dependency

Include the Kpow Streams Agent library in your application:

```
implementation 'io.factorhouse:kpow-streams-agent:1.0.0'
```

### Integrate the Agent

We define a new Component that adds an event-listener to the StreamsBuilderFactoryBean, registering the Streams and Topology once the Kafka Streams application has been started.

```Java
package io.confluent.kafkadevops.microservicesorders.ordersservice;

import io.factorhouse.kpow.StreamsRegistry;
import io.factorhouse.kpow.key.ClusterIdKeyStrategy;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class KpowAgentService {

  @Autowired
  public KpowAgentService(final StreamsBuilderFactoryBean factory) {

    factory.addListener(new StreamsBuilderFactoryBean.Listener() {
      @Override
      public void streamsAdded(@NonNull String id, @NonNull KafkaStreams streams) {
        Topology topology = factory.getTopology();

        // Create a Kpow StreamsRegistry connecting to the same Kafka Cluster as Kafka Streams.
        // Note: In a multi-cluster Kpow setup the StreamsRegistry must be configured with your Primary cluster.
        StreamsRegistry registry = new StreamsRegistry(factory.getStreamsConfiguration());

        // Specify the key strategy when writing metrics to the internal Kafka topic
        // props are java.util.Properties describing the Kafka Connection
        ClusterIdKeyStrategy keyStrat = new ClusterIdKeyStrategy(factory.getStreamsConfiguration());
        
        // Register your KafkaStreams and Topology instances with the StreamsRegistry
        registry.register(streams, topology, keyStrat);
      }
    });
  }
}
```

----

### Original Project Readme Follows

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

Configuration can be overridden using [many methods](https://docs.spring.io/spring-boot/docs/1.5.6.RELEASE/reference/html/boot-features-external-config.html).
