package dev.java10x.MagicFridgeAI.service;

import dev.java10x.MagicFridgeAI.model.FoodItem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatGptService {

    private final WebClient webClient;
    private String apiKey = System.getenv("API_KEY_CHAT_GPT");

    public ChatGptService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> generateRecipe(List<FoodItem> fooditems) {

        String alimentos = fooditems.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %d, Validade: %s",
                        item.getNome(), item.getCategoria(),
                        item.getQuantidade(), item.getValidade()))
                .collect(Collectors.joining("\n"));

//        String prompt = "Agora você é um chef de cozinha e vai me sugerir receitas com base nos ingredientes que vou te passar, ok?";
//        String prompt = "Sugira uma receita simples com ingredientes comuns porem sou alergico a massa e a alho.";
        String prompt = "Baseado no meu banco de dados faca uma receita com os seguintes items:\n " + alimentos;

        Map<String, Object> requestBody = Map.of(
             "model", "gpt-4o",
             "messages", List.of(
                 Map.of("role", "system", "content", "Você é um assistente que cria receitas"),
                 Map.of("role", "user", "content", prompt)
             )
        );

        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        return message.get("content").toString();
                    }
                    return "Nenhuma receita foi gerada.";
                });
    }
}
