package com.mithun.ai_day1.service;

import com.mithun.ai_day1.exception.AiServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AskService {

    private static final Logger log = LoggerFactory.getLogger(AskService.class);

    private static final String SYSTEM_PROMPT =
            "You are a concise technical assistant. Reply in plain text only. "
          + "No Markdown, no headings, no bullet symbols.";

    private final ChatClient chatClient;

    public AskService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String ask(String question) {
        try {
            return chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(question)
                    .call()
                    .content();
        } catch (Exception ex) {
            log.error("Claude API call failed: {}", ex.getMessage(), ex);
            throw new AiServiceException("AI service is temporarily unavailable", ex);
        }
    }
}
