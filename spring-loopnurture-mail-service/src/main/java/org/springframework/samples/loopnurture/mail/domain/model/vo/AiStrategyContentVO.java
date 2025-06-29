package org.springframework.samples.loopnurture.mail.domain.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value object representing the structured JSON stored in {@code AiStrategyDO#aiStrategyContent}.
 * <p>
 * The JSON contains two main prompts generated/used during email creation:
 * 1. {@code intentPrompt}  – the prompt sent to Claude for intent analysis.
 * 2. {@code emailHtmlPrompt} – the prompt (with previous context) sent to Claude for HTML email generation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiStrategyContentVO {

    /** Prompt used for intent parsing step (claude-sonnet-4-20250514). */
    private String intentPrompt;

    /** Prompt used for HTML email generation step (claude-sonnet-4-20250514). */
    private String emailHtmlPrompt;

    /** Prompt used for email content (plain text) generation step. */
    private String emailContentPrompt;
} 