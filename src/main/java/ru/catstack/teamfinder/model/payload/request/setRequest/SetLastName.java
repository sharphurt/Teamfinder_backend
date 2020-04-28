package ru.catstack.teamfinder.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Update last name Request", description = "The update last name request payload")
public class SetLastName {
    @NotBlank(message = "Last name cannot be blank")
    @ApiModelProperty(value = "A valid last name", required = true, allowableValues = "NonEmpty String")
    private String lastName;

    public SetLastName(String lastName) {
        this.lastName = lastName;
    }

    public SetLastName() {

    }

    public String getLastName() {
        return lastName;
    }
}

