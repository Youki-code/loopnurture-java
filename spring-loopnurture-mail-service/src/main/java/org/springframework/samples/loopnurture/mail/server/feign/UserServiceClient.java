package org.springframework.samples.loopnurture.mail.server.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/validate-token")
    boolean validateToken(@RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/users/current")
    String getCurrentUser(@RequestHeader("Authorization") String token);
} 