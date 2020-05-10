package ru.catstack.teamfinder.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "Team card registration Request", description = "The team card registration request payload")
public class TeamRegistrationRequest {

    @NotBlank(message = "Team name cannot be blank")
    @Pattern(regexp = "[a-zA-Z0-9а-яА-Я\\s]{2,30}", message = "The team name can only include uppercase and lowercase letters of the English and Russian alphabet and numbers and length must be between 2 and 30 character")
    @ApiModelProperty(value = "A valid team name", required = true, allowableValues = "NonEmpty String")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @ApiModelProperty(value = "Team description", required = true, allowableValues = "NonEmpty String")
    @Length(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @ApiModelProperty(value = "Picture base64 code")
    private String picCode;

    public TeamRegistrationRequest(String name, String description, String picCode) {
        this.name = name;
        this.description = description;
        this.picCode = picCode;
    }

    public TeamRegistrationRequest() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPicCode() {
        return picCode;
    }
}
