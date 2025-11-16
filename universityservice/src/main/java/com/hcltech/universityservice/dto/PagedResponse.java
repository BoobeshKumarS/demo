package com.hcltech.universityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PagedResponse<T> {
    private boolean success;
    private long count;
    private Pagination pagination;
    private List<T> data;

    @Data
    @AllArgsConstructor
    public static class Pagination {
        private int page;
        private int limit;
        private int totalPages;
    }
}