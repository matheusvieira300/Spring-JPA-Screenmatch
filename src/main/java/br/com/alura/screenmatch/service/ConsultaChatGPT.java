package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService(System.getenv("${OPENAI_APIKEY}"));


        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000) //quanto mais tokens mais completa é a resposta dele
                .temperature(0.7)
                .build();


          try {
            CompletionResult resposta = service.createCompletion(requisicao);
            return resposta.getChoices().get(0).getText().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao obter tradução: " + e.getMessage();
        }
    }
}
