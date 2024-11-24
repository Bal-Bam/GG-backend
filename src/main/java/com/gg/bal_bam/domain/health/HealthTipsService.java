package com.gg.bal_bam.domain.health;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HealthTipsService {

    private final OpenAiService openAiService;

    public String generateHealthTip() {

        ChatMessage systemMessage = new ChatMessage("systme", "간단한 한 줄 정도의 오늘의 건강 팁을 제시해줘.)");
        ChatMessage userMessage = new ChatMessage("user", "오늘의 건강 팁을 정말 간단하게 알려줘.");

        // api 요청
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4")
                .messages(Collections.singletonList(userMessage))
                .maxTokens(100)
                .build();

        return openAiService.createChatCompletion(request).getChoices().get(0).getMessage().getContent();
    }
}
