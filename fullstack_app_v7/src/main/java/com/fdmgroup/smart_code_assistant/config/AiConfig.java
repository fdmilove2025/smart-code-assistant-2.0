package com.fdmgroup.smart_code_assistant.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    
    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient() {
        OllamaApi ollamaApi = new OllamaApi();
        return new OllamaChatClient(ollamaApi)
            .withModel("qwen2.5-coder:0.5b");
    }
    
    @Bean(name = "deepseekChatClient")
    public ChatClient deepseekChatClient() {
        OllamaApi ollamaApi = new OllamaApi();
        return new OllamaChatClient(ollamaApi)
            .withModel("deepseek-r1:8b");
    }
} 