package ru.catstack.teamfinder.dto;

import lombok.Data;

@Data
class AuthenticationRequestDto {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
