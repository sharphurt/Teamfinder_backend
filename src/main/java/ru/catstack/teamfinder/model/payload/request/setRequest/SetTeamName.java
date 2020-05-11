package ru.catstack.teamfinder.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value = "Update team name Request", description = "The update team name request payload")
public class SetTeamName {

    @Pattern(regexp = "[a-zA-Z0-9а-яА-Я\\s]{2,30}", message = "The team name can only include uppercase and lowercase letters of the English and Russian alphabet and numbers and length must be between 2 and 30 character")
    @ApiModelProperty(value = "A valid team name", required = true, allowableValues = "NonEmpty String")
    private String name;

    public SetTeamName(String newName) {
        this.name = newName;
    }

    public SetTeamName() {

    }

    public String getName() {
        return name;
    }
}

