package com.blackcode.external_pagination_service.utils;

import com.blackcode.external_pagination_service.dto.PaginationResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PaginationUtils {

    public <T> PaginationResponse<T> paginate(List<T> data, int page, int size) {
        if (page <= 0 || size <= 0) {
            throw new IllegalArgumentException("Page and size must be greater than 0");
        }

        int totalItems = data.size();
        int totalPages = totalItems == 0 ? 0 : (int) Math.ceil((double) totalItems / size);

        if (totalItems == 0 || page > totalPages) {
            return new PaginationResponse<>(page, size, totalItems, totalPages, Collections.emptyList());
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalItems);

        List<T> pagedData = data.subList(start, end);

        return new PaginationResponse<>(page, size, totalItems, totalPages, pagedData);
    }

}
