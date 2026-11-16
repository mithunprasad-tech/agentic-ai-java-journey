package com.mithun.ai_day1.controller;

import com.mithun.ai_day1.dto.AskRequest;
import com.mithun.ai_day1.dto.AskResponse;
import com.mithun.ai_day1.service.AskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    private final AskService askService;

    public AskController(AskService askService) {
        this.askService = askService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@Valid @RequestBody AskRequest request) {
        return new AskResponse(askService.ask(request.question()));
    }
}
