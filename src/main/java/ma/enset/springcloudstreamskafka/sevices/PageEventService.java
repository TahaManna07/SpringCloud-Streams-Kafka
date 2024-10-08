package ma.enset.springcloudstreamskafka.sevices;

import ma.enset.springcloudstreamskafka.entities.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

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

}


