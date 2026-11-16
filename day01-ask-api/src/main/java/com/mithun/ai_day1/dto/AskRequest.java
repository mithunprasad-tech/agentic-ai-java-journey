package com.mithun.ai_day1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AskRequest(
        @NotBlank(message = "Question must not be empty")
        @Size(max = 500, message = "Question must be 500 characters or less")
        String question
) {}