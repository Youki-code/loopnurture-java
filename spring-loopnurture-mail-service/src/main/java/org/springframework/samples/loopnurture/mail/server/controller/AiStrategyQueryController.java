package org.springframework.samples.loopnurture.mail.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.domain.enums.AiStrategyTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.model.AiStrategyDO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.samples.loopnurture.mail.server.controller.dto.GetAiStrategyRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.AiStrategyResponse;
import org.springframework.samples.loopnurture.mail.service.AiStrategyQueryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 策略查询控制器
 */
@RestController
@RequestMapping("/api/v1/ai/strategy")
@RequiredArgsConstructor
@RequireLogin
public class AiStrategyQueryController {

    private final AiStrategyQueryService aiStrategyQueryService;

    /**
     * 根据策略类型获取最新启用策略
     */
    @PostMapping("/getLatestStrategy")
    public ApiResponse<AiStrategyResponse> getLatestStrategy(@Valid @RequestBody GetAiStrategyRequest request) {
        // 如果没有传入类型的话，默认获取营销邮件的策略
        if(request.getAiStrategyType() == null){
            request.setAiStrategyType(AiStrategyTypeEnum.MARKETING_MAIL.getCode());
        }
        AiStrategyDO strategy = aiStrategyQueryService.getLatestEnabledStrategy(request.getAiStrategyType());
        if (strategy == null) {
            return ApiResponse.error("Strategy not found");
        }
        AiStrategyResponse resp = new AiStrategyResponse();
        resp.setAiStrategyVersion(strategy.getAiStrategyVersion());
        resp.setAiStrategyContent(strategy.getAiStrategyContent());
        resp.setAiStrategyType(strategy.getAiStrategyType()!=null?strategy.getAiStrategyType().getCode():null);
        return ApiResponse.ok(resp);
    }
} 