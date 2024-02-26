package org.maxym.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.maxym.spring.model.Measurement;
import org.maxym.spring.model.Sensor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Hello world!
 */
public class Client {

    private final WebClient webClient;

    public Client() {
        ObjectMapper mapper = new ObjectMapper();
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(clientDefaultCodecsConfigurer -> {
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
                    clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
                }).build();
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("http://localhost:8080")
                .build();
    }

    public static void main(String[] args) {
        Client client = new Client();

        Random random = new Random();

        List<Sensor> sensors = new ArrayList<Sensor>() {{
            add(new Sensor("Sensor1"));
            add(new Sensor("Sensor2"));
            add(new Sensor("Sensor3"));
        }};

        Stream.generate(() -> new Measurement(random.nextDouble() * 200 - 100, random.nextBoolean(), sensors.get(random.nextInt(3))))
                .limit(1000)
                .map(client::registerMeasurements)
                .forEach(System.out::println);
    }

    public String registerSensor(Sensor sensor) {
        try {
            webClient.post()
                    .uri("/sensors/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(sensor)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(error ->
                                            Mono.error(new RuntimeException(error))))
                    .bodyToMono(Void.class)
                    .block();
            return "OK";
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
    }

    public String registerMeasurements(Measurement measurement) {
        try {
            webClient.post()
                    .uri("/measurements/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(measurement)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .flatMap(error ->
                                            Mono.error(new RuntimeException(error))))
                    .bodyToMono(Void.class)
                    .block();
            return "OK";
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
    }
}
