package ru.catstack.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminUserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Byte age;
    private String email;
    private String status;

    private AdminUserDto(Long id, String username, String firstName, String lastName, Byte age, String email, String status) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static AdminUserDto fromUser(User user) {
        return new AdminUserDto(user.getId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getAge(), user.getEmail(), user.getStatus().name());
    }
}
