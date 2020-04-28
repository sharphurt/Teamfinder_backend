package ru.catstack.teamfinder.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Update first name Request", description = "The update first name request payload")
public class SetFirstName {
    @NotBlank(message = "First name cannot be blank")
    @ApiModelProperty(value = "A valid first name", required = true, allowableValues = "NonEmpty String")
    private String firstName;

    public SetFirstName(String firstName) {
        this.firstName = firstName;
    }

    public SetFirstName() {

    }

    public String getFirstName() {
        return firstName;
    }
}

