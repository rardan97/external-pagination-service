package com.blackcode.external_pagination_service.service;

import com.blackcode.external_pagination_service.dto.PaginationResponse;
import com.blackcode.external_pagination_service.model.User;
import com.blackcode.external_pagination_service.utils.PaginationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final CallApiService callApiService;

    private final PaginationUtils paginationUtils;

    public UserService(CallApiService callApiService, PaginationUtils paginationUtils) {
        this.callApiService = callApiService;
        this.paginationUtils = paginationUtils;
    }

    public PaginationResponse<User> getPaginatedUsers(int page, int size, String name) {
        logger.info("Requested paginated users - page: {}, size: {}, filter: {}", page, size, name);
        if (page <= 0 || size <= 0) {
            logger.error("Invalid pagination parameters - page: {}, size: {}", page, size);
            throw new IllegalArgumentException("Page and size must be greater than 0");
        }

        List<User> allUsers = callApiService.getAllUsers();

        if (name != null && !name.isBlank()) {
            String keyword = name.toLowerCase();
            allUsers = allUsers.stream().filter(u ->
                                    u.getFirstName().toLowerCase().contains(keyword) ||
                                    u.getLastName().toLowerCase().contains(keyword)).toList();

            logger.info("Filtered users by name '{}', remaining users: {}", name, allUsers.size());
        }

        PaginationResponse<User> response = paginationUtils.paginate(allUsers, page, size);
        logger.info("Returning {} users for page {}", response.getData().size(), page);

        return response;
    }

}