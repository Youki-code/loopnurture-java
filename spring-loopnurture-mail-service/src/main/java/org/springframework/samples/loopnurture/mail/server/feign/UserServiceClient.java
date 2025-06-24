package org.springframework.samples.loopnurture.mail.server.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${services.user.url}")
public interface UserServiceClient {

    @PostMapping("/api/v1/auth/validate")
    ApiResponse<org.springframework.samples.loopnurture.mail.server.feign.dto.TokenValidationResponse> validateToken(
            @RequestBody org.springframework.samples.loopnurture.mail.server.feign.dto.ValidateTokenRequest request);
} 