package com.mithun.ai_day1.service;

import com.mithun.ai_day1.dto.RefundDecision;
import com.mithun.ai_day1.dto.RefundRequest;
import com.mithun.ai_day1.exception.AiServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RefundService {

    private static final Logger log = LoggerFactory.getLogger(RefundService.class);

    // Hard limit enforced by CODE, not by the AI
    private static final int AUTO_APPROVAL_LIMIT = 1000;

    private static final String POLICY = """
            You are a refund assistant for an online store.
            Refund policy:
            - If an order is more than 7 days late, the customer is eligible for a full refund.
            - If an order is 7 days late or less, the customer is not eligible.
            - The refund amount can never be more than the order amount.
            Explain your reason in one short sentence.
            """;

    private final ChatClient chatClient;

    public RefundService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public RefundDecision decide(RefundRequest request) {
        RefundDecision aiDecision;
        try {
            aiDecision = chatClient.prompt()
                    .system(POLICY)
                    .user(u -> u.text("""
                            Order ID: {orderId}
                            Days late: {daysLate}
                            Order amount (INR): {orderAmount}
                            Customer message: {message}
                            """)
                            .param("orderId", request.orderId())
                            .param("daysLate", String.valueOf(request.daysLate()))
                            .param("orderAmount", String.valueOf(request.orderAmount()))
                            .param("message", request.customerMessage()))
                    .call()
                    .entity(RefundDecision.class);
        } catch (Exception ex) {
            log.error("Claude API call failed: {}", ex.getMessage(), ex);
            throw new AiServiceException("AI service is temporarily unavailable", ex);
        }

        return applyGuardrails(aiDecision, request);
    }

    // Code checks the AI's decision
    private RefundDecision applyGuardrails(RefundDecision ai, RefundRequest request) {
        if (ai == null) {
            throw new AiServiceException("AI returned no decision", null);
        }

        int amount = ai.refundAmount();

        // Rule 1: never refund more than the order amount
        if (amount > request.orderAmount()) {
            log.warn("AI suggested {} for order amount {}. Capping.", amount, request.orderAmount());
            amount = request.orderAmount();
        }

        // Rule 2: not eligible means no refund
        if (!ai.eligible()) {
            amount = 0;
        }

        // Rule 3: large refunds always need a human
        boolean needsHuman = ai.needsHumanApproval() || amount > AUTO_APPROVAL_LIMIT;

        return new RefundDecision(ai.eligible(), amount, ai.reason(), needsHuman);
    }
}
