package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通用分页响应DTO
 */
@Data
public class PageResponse<T> {
    private long total;
    private int pages;
    private List<T> content;

    public static <T, R> PageResponse<R> from(Page<T> page, Function<T, R> converter) {
        PageResponse<R> response = new PageResponse<>();
        response.setTotal(page.getTotalElements());
        response.setPages(page.getTotalPages());
        response.setContent(page.getContent().stream()
            .map(converter)
            .collect(Collectors.toList()));
        return response;
    }
} 