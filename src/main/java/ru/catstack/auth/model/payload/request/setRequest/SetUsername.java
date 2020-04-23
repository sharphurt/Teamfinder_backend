package ru.catstack.auth.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

@ApiModel(value = "Update username Request", description = "The update username request payload")
public class SetUsername {
    @NotBlank(message = "Username cannot be blank")
    @NotNull(message = "Username cannot be null")
    @Pattern(regexp = "[a-zA-Z0-9]{2,30}", message = "The username can only include uppercase and lowercase letters of the English alphabet and numbers and length must be between 2 and 30 character")
    @ApiModelProperty(value = "A valid username", required = true, allowableValues = "NonEmpty String")
    private String username;

    public SetUsername(String newUsername) {
        this.username = newUsername;
    }

    public SetUsername() {

    }

    public String getUsername() {
        return username;
    }
}

