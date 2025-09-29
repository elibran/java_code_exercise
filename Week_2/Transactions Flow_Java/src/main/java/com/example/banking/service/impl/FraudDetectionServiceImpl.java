
package com.example.banking.service.impl;

import com.example.banking.service.FraudDetectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final WebClient webClient;

    public FraudDetectionServiceImpl(@Value("${fraud.base-url:http://localhost:9090}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public boolean isTransactionFraudulent(Long fromAccountId, BigDecimal amount) {
        try {
            Map<String, Object> payload = Map.of(
                    "accountId", fromAccountId,
                    "amount", amount.toPlainString()
            );
            // 5-second timeout to avoid holding DB transaction too long
            Boolean result = webClient.post()
                    .uri("/fraud/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(payload))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(5))
                    .map(map -> (Boolean) map.getOrDefault("isFraudulent", false))
                    .onErrorResume(ex -> Mono.error(new RuntimeException("Fraud service unavailable", ex)))
                    .block();
            return Boolean.TRUE.equals(result);
        } catch (Exception ex) {
            // In real life, choose fail-open/closed policy carefully. We'll fail-closed here.
            throw new RuntimeException("Fraud service unavailable", ex);
        }
    }
}
