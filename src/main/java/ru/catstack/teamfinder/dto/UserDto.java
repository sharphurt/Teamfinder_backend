package ru.catstack.teamfinder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.catstack.teamfinder.model.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String email;

    private UserDto(Long id, String username, String email)
    {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public static UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
