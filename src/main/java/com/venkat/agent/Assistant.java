package com.venkat.agent;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface Assistant {

    @SystemMessage("""
            You solve problems step by step using the available tools.
            Call a tool when you need a calculation, a word count, or the current time.
            When you have enough information, answer directly and concisely.
            """)
    String chat(@MemoryId String userId, @UserMessage String message);
}
