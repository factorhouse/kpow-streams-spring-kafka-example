package io.confluent.kafkadevops.microservicesorders.ordersservice;

import io.operatr.kpow.StreamsRegistry;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class kPowAgentService {

  @Autowired
  public kPowAgentService(final StreamsBuilderFactoryBean factory) {

    factory.addListener(new StreamsBuilderFactoryBean.Listener() {
      @Override
      public void streamsAdded(@NonNull String id, @NonNull KafkaStreams streams) {
        Topology topology = factory.getTopology();

        // Create a kPow StreamsRegistry connecting to the same Kafka Cluster as Kafka Streams.
        // Note: In a multi-cluster kPow setup the StreamsRegistry must be configured with your Primary cluster.
        StreamsRegistry registry = new StreamsRegistry(factory.getStreamsConfiguration());

        // Register your KafkaStreams and Topology instances with the StreamsRegistry
        registry.register(streams, topology);
      }
    });
  }
}
