package com.ssc.aiservice.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GemmaService {


    private ChatClient chatClient;

    public GemmaService(OllamaChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }


    public String processContent(String question) {
        // Query the AI model API
        String response = chatClient.prompt()
                .user(question)
                .call()
                .content();

        // 3. Return the parsed response
        return response;
    }

}
