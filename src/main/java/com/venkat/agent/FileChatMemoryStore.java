package com.venkat.agent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

public class FileChatMemoryStore implements ChatMemoryStore {
    private final Path dir;

    public FileChatMemoryStore() {
        this(Path.of("chat-memory"));
    }

    public FileChatMemoryStore(Path dir) {
        this.dir = dir;
    }

    private Path fileFor(Object memoryId) {
        String safe = String.valueOf(memoryId).replaceAll("[^a-zA-Z0-9-_]", "_");
        return dir.resolve("memory-" + safe + ".json");
    }
    @Override 
    public List<ChatMessage> getMessages(Object memoryId) {
        System.out.println("--In GetMessgaes() --MemoryId"+memoryId );
        Path file = fileFor(memoryId);
        if (!Files.exists(file)) {
            return List.of(); // empty, never null — first run has no history
        }
        try {
            String json = Files.readString(file);
            return ChatMessageDeserializer.messagesFromJson(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read chat memory for " + memoryId, e);
        }
    }
    @Override
    public void updateMessages(Object memoryId,List<ChatMessage> messages){
         System.out.println("--In UpdateMessages() -- With MemoryId"+memoryId +"and Messages "+messages);
        try
        {
           Files.createDirectories(dir);
           String json = ChatMessageSerializer.messagesToJson(messages);
            Files.writeString(fileFor(memoryId), json);
            System.out.println("[store] wrote " + messages.size() + " messages for " + memoryId);
        } catch(IOException e){
            throw new RuntimeException("Failed to write chat memory for " + memoryId, e);
        }
    }
    @Override 
    public void deleteMessages(Object memoryId) {
        System.out.println("-- In DeleteMessages() --MemoryId\"+memoryId ");

        try {
            Files.deleteIfExists(fileFor(memoryId));

        } catch (IOException e) {
                        throw new RuntimeException("Failed to delete chat memory for " + memoryId, e);
        }
    }
    
}
