package com.venkat.agent;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class WordCountTool {

    @Tool("Count the words in a text and return the count")
    public String countWords(@P("the text to count words in") String text) {
        System.out.println("[TOOL] countWords called with: "+ text);
        if (text == null || text.isBlank()) return "0";
        String word_count = String.valueOf(text.trim().split("\\s+").length);
        System.out.println("Word Cont is --> "+ word_count);
        
        return word_count ;
    }
}
