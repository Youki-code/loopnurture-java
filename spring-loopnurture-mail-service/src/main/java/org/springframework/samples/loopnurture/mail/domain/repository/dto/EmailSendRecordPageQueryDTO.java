package org.springframework.samples.loopnurture.mail.domain.repository.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmailSendRecordPageQueryDTO {
    private int pageNum = 0;
    private int pageSize = 20;
    private List<Integer> enableStatusList;
} 