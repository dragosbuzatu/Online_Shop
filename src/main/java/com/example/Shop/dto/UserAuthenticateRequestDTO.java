package com.example.Shop.dto;

import lombok.Data;

@Data
public class UserAuthenticateRequestDTO {
    private String username;
    private String password;
}
