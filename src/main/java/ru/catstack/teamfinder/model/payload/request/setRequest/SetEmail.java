package ru.catstack.teamfinder.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "Update age Request", description = "The update age request payload")
public class SetEmail {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    public SetEmail(String firstName) {
        this.email = firstName;
    }

    public SetEmail() {

    }

    public String getEmail() {
        return email;
    }
}

