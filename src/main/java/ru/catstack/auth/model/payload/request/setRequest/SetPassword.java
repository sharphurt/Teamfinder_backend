package ru.catstack.auth.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Update age Request", description = "The update age request payload")
public class SetPassword {
    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8, max = 50, message = "Password length must be between 8 and 50 characters")
    @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;

    public SetPassword(String firstName) {
        this.password = firstName;
    }

    public SetPassword() {

    }

    public String getPassword() {
        return password;
    }
}

