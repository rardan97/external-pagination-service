package com.blackcode.external_pagination_service.service;

import com.blackcode.external_pagination_service.dto.DummyApiResponse;
import com.blackcode.external_pagination_service.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CallApiService {

    private static final Logger logger = LoggerFactory.getLogger(CallApiService.class);

    private final String externalApiUrl;
    private final Cache<String, List<User>> userCache;
    private final ObjectMapper objectMapper;

    public CallApiService(
            @Value("${external-api.url}") String externalApiUrl,
            Cache<String, List<User>> userCache,
            ObjectMapper objectMapper) {

        this.externalApiUrl = externalApiUrl;
        this.userCache = userCache;
        this.objectMapper = objectMapper;
    }

    public List<User> getAllUsers() {
        logger.info("Fetching users from cache...");
        return userCache.get("users",
                key -> {
                        logger.info("Cache miss! Fetching users from external API...");
                        return fetchUsersFromApi();
        });
    }

    private List<User> fetchUsersFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            logger.info("Calling external API: {}", externalApiUrl);
            String jsonResponse = restTemplate.getForObject(externalApiUrl, String.class);
            DummyApiResponse response = objectMapper.readValue(jsonResponse, DummyApiResponse.class);

            if (response == null || response.getUsers() == null) {
                logger.error("Invalid response from external API");
                throw new RuntimeException("Invalid response from external API");
            }
            logger.info("Successfully fetched {} users from API", response.getUsers().size());
            return response.getUsers();
        } catch (Exception e) {
            logger.error("External API unreachable: {}", e.getMessage());
            throw new RuntimeException("External API unreachable", e);
        }
    }

}
