package ma.enset.springcloudstreamskafka.sevices;

import ma.enset.springcloudstreamskafka.entities.PageEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// Assurez-vous d'utiliser une interface pour le binding
@Service

public class PageEventService {
    public PageEventService() {
        System.out.println("Le service de consommation est actif.");
    }
    // il va consumer les objects de type PageEvent
    @Bean
    public Consumer<PageEvent> pageEventConsumer(){
        return (input)->{
            System.out.println("*********************");
            System.out.println(input.toString());
            System.out.println("*********************");
        };
    }

    @Bean
    public Supplier<PageEvent> pageEventSupplier(){
        return ()->new  PageEvent(Math.random()>0.5?"P1":"P2",
                Math.random()>0.5?"USER1":"USER2",
                new Date(),
                new Random().nextInt(9000));
    }
    @Bean
    public Function<PageEvent,PageEvent> pageEventFunction(){
        return (input)->{
            input.setName("Page event ");
            input.setUser("Taha retourne");
            return input;
        };

    }
    @Bean
    public Function<KStream<String, PageEvent>,KStream <String,Long>> kStreamFunction(){
        return (input)->{
            return input
                    .filter((k,v)->v.getDuration()>100)
                    .map((k,v)->new KeyValue<>(v.getName(),0L))
                    .groupBy((k,v)->k,Grouped.with(Serdes.String(),Serdes.Long()))
                    .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))
                    .count(Materialized.as("page-count"))
                    .toStream()
                    .map((k,v)->new KeyValue<>("=>"+k.window().startTime()+k.window().endTime()+k.key(),v));
        };
    }

}


