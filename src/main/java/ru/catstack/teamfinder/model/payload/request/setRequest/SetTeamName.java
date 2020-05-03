package ru.catstack.teamfinder.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(value = "Update team name Request", description = "The update team name request payload")
public class SetTeamName {
    @NotBlank(message = "Team name cannot be blank")
    @NotNull(message = "Team name cannot be null")
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

