package com.mithun.ai_day1.controller;

import com.mithun.ai_day1.dto.RefundDecision;
import com.mithun.ai_day1.dto.RefundRequest;
import com.mithun.ai_day1.service.RefundService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/refund-decision")
    public RefundDecision decide(@Valid @RequestBody RefundRequest request) {
        return refundService.decide(request);
    }
}
