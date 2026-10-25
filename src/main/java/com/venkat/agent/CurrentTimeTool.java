package com.venkat.agent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dev.langchain4j.agent.tool.Tool;

public class CurrentTimeTool {

    @Tool("Returns the current local date and time. Call this for any question about the current time or date.")
    public String getCurrentTime() {
        System.out.println("[TOOL] getCurrentTime called with: ");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
