package org.maxym.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.maxym.spring.model.Sensor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

        Sensor sensor = new Sensor("Sensor");
        String response = client.registerSensor(sensor);

        System.out.println(response);
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
}
