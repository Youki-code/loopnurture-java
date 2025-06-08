package org.springframework.samples.loopnurture.mail.server.context;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * 用户上下文
 */
@Data
@Component
@RequestScope
public class UserContext {
    private String userId;
    private String orgId;
    private String username;

    public void clear() {
        this.userId = null;
        this.orgId = null;
        this.username = null;
    }
} 