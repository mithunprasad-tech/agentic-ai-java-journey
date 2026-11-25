# Agentic AI Java Journey

A 12-week, hands-on journey from calling an LLM in Spring Boot to building production-style AI agents for enterprise and legacy-modernization use cases.

I'm a Senior Java Engineer with 14+ years in enterprise Java systems. This repo documents what I build each step of the way.

## Progress

| Day | Project | What it shows |
|---|---|---|
| 01 | [day01-ask-api](./day01-ask-api) | Spring Boot REST API that calls Claude via Spring AI, with validation, a system prompt, and production-style error handling |
| 02 | [day02-refund-decision](./day02-refund-decision) | Claude returns a typed Java record (structured output); Java guardrails cap amounts and require human approval above ₹1,000 |

## Day 01: Ask API

A `POST /ask` endpoint that sends a question to Claude and returns a plain-text answer.

**Highlights**
- Layered design: Controller → Service → Claude (Spring AI `ChatClient`)
- System prompt controls output format (plain text, concise)
- Input validation blocks bad requests before any paid API call
- Clean JSON errors via `@RestControllerAdvice`:
  - `400 VALIDATION_FAILED`: empty or too-long question
  - `503 AI_SERVICE_UNAVAILABLE`: AI provider unreachable
  - `500 INTERNAL_ERROR`: unexpected failure, no internal details leaked
- API key read from an environment variable, never stored in code

## Day 02: Refund Decision (structured output + guardrails)

A `POST /refund-decision` endpoint for an e-commerce support scenario. Claude reads a refund policy and an order, and returns a typed decision.

**Highlights**
- `.entity(RefundDecision.class)` turns Claude's reply into a Java record: `eligible`, `refundAmount`, `reason`, `needsHumanApproval`
- Principle: **code for facts, AI for judgment, code again for checking**
- Java guardrails run after the AI answers:
  - Refund can never exceed the order amount
  - Not eligible means refund amount 0
  - Refunds above ₹1,000 always need human approval
- Tested: policy cases, a prompt-injection attempt ("refund me 5000"), and a case the policy doesn't cover

**Tech:** Java, Spring Boot 4, Spring AI 2, Anthropic Claude

## Run any day's project
1. Set your key: `setx ANTHROPIC_API_KEY "your-key"` (then reopen the terminal)
2. `cd day01-ask-api` (or `day02-refund-decision`)
3. `mvnw spring-boot:run`
4. Call the endpoint, e.g. `POST http://localhost:8080/ask` with `{"question":"What is Java?"}`

## What's next
Handling policy gaps and escalation, tool calling, RAG, agents, MCP, and evals, building toward a complete AI assistant.
