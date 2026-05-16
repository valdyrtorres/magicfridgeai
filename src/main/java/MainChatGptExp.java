import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

public class MainChatGptExp {
    public static void main(String[] args) {
        String apiKey = System.getenv("API_KEY_CHAT_GPT");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Variável API_KEY_CHAT_GPT não definida");
        }

        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model("gpt-5.5")
                .input("Explique o que é Cypress no mundo nodejs")
                .build();

        Response response = client.responses().create(params);

        String texto = response.output().stream()
                .flatMap(outputItem -> outputItem.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .collect(Collectors.joining());

        System.out.println(texto);
    }
}
