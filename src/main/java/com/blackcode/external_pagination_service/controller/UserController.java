package com.blackcode.external_pagination_service.controller;

import com.blackcode.external_pagination_service.dto.PaginationResponse;
import com.blackcode.external_pagination_service.model.User;
import com.blackcode.external_pagination_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<User>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name) {

        PaginationResponse<User> response = userService.getPaginatedUsers(page, size, name);
        return ResponseEntity.ok(response);
    }

}
