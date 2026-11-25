package com.mithun.ai_day1.dto;

public record RefundDecision(
        boolean eligible,
        int refundAmount,
        String reason,
        boolean needsHumanApproval
) {}
