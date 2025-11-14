package com.blackcode.external_pagination_service.dto;

import com.blackcode.external_pagination_service.model.User;
import lombok.Data;

import java.util.List;

@Data
public class DummyApiResponse {

    private List<User> users;

}
