package com.venkat.agent;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class Main {

    public static void main(String[] args) {
        String question = args.length > 0 ? String.join(" ", args) : "What is 15% of 240?";
        String memoryId = System.getenv().getOrDefault("MEMORY_ID", "user-1");

        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-4o-mini")
                .temperature(0.0)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemoryProvider(id -> MessageWindowChatMemory.builder()
                        .id(id)
                        .maxMessages(10)
                        .chatMemoryStore(new FileChatMemoryStore())
                        .build())
                .tools(new CalculatorTool(), new WordCountTool(), new CurrentTimeTool())
                .build();

        System.out.println(assistant.chat(memoryId, question));
    }
}
