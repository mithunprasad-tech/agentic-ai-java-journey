package com.mithun.ai_day1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefundRequest(
        @NotBlank(message = "Order ID is required")
        String orderId,

        @Min(value = 0, message = "Days late cannot be negative")
        int daysLate,

        @Min(value = 1, message = "Order amount must be positive")
        int orderAmount,

        @NotBlank(message = "Customer message is required")
        @Size(max = 500, message = "Customer message must be 500 characters or less")
        String customerMessage
) {}
