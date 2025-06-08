package org.springframework.samples.loopnurture.mail.context;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Data
@Component
@RequestScope
public class UserContext {
    private String token;
    private String userId;
    private String orgId;
    private String username;
} 