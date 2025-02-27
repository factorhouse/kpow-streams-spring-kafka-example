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
