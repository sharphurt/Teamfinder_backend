package ru.catstack.teamfinder.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Update first name Request", description = "The update first name request payload")
public class SetAboutMe {
    @ApiModelProperty(value = "A valid about me info", required = true, allowableValues = "NonEmpty String")
    @Length(min = 0, max = 500, message = "Text length must be >= 0 and < 500 characters")
    private String about;

    public SetAboutMe(String firstName) {
        this.about = firstName;
    }

    public SetAboutMe() {

    }

    public String getAbout() {
        return about;
    }
}

